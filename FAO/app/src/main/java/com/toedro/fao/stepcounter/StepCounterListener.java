package com.toedro.fao.stepcounter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.toedro.fao.App;
import com.toedro.fao.db.Step;
import com.toedro.fao.ui.Utils;
import com.toedro.fao.ui.settings.ProgressTypeHome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class StepCounterListener implements SensorEventListener {
    private static StepCounterListener instance;

    private int stepsCompleted;
    private ProgressTypeHome pth;
    private TextView stepsCountTextView;

    private ArrayList<Integer> mACCSeries = new ArrayList<Integer>();
    private ArrayList<String> mTimeSeries = new ArrayList<String>();

    private double accMag = 0d;
    private int lastXPoint = 1;
    private int stepThreshold = 10;

    private String timestamp;
    private String day;
    private String hour;

    private static boolean active = false;

    // Constructor, get steps completed, TextView and ProgressTypeHome as args
    private StepCounterListener(int steps, TextView tv, ProgressTypeHome pth)  throws StepCounterListenerException {
        if (instance != null) {
            throw new StepCounterListenerException();
        }
        stepsCompleted = steps;
        stepsCountTextView = tv;
        this.pth = pth;
    }

    public static StepCounterListener getInstance(int steps, TextView tv, ProgressTypeHome pth) {
        if (instance == null) {
            try {
                instance = new StepCounterListener(steps, tv, pth);
            } catch (Exception e) { }
        } else {
            instance.setProgressTypeHome(pth);
            instance.setTextView(tv);
        }
        return instance;
    }

    private void setTextView(TextView tv) {
        stepsCountTextView = tv;
    }

    private void setProgressTypeHome(ProgressTypeHome progress) {
        pth = progress;
    }

    public void activate() {
        active = true;
    }
    public void deactivate() {
        active = false;
    }
    public static boolean isActive() {
        return active;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the timestamp
        long timeInMillis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;
        // Convert the timestamp to date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String date = jdf.format(timeInMillis);
        // Get the date, the day and the hour from the date
        timestamp = date;
        day = date.substring(0,10);
        hour = date.substring(11,13);

        switch (event.sensor.getType()) {
            // ACCELEROMETER
            case Sensor.TYPE_LINEAR_ACCELERATION:
                // Get x, y and z
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                // Calculate the magnitude
                accMag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
                // Update the Magnitude series
                mACCSeries.add((int) accMag);
                // Update the time series
                mTimeSeries.add(timestamp);
                // Calculate ACC peaks and steps
                peakDetection();
                break;
            // STEP DETECTOR
            case Sensor.TYPE_STEP_DETECTOR:
                // Calculate the number of steps
                countSteps(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void peakDetection() {
        // Peak detection algorithm derived from:
        // A Step Counter Service for Java-Enabled Devices Using a Built-In Accelerometer,
        // Mladenov et al.

        int windowSize = 20;
        // Get the length of the series
        int highestValX = mACCSeries.size();
        // If the segment is smaller than the processing window skip it
        if (highestValX - lastXPoint < windowSize) {
            return;
        }

        List<Integer> valuesInWindow = mACCSeries.subList(lastXPoint,highestValX);
        List<String> timesInWindow = mTimeSeries.subList(lastXPoint,highestValX);

        lastXPoint = highestValX;

        int forwardSlope = 0;
        int downwardSlope = 0;

        List<Integer> dataPointList = new ArrayList<Integer>();
        List<String> timePointList = new ArrayList<String>();

        for (int p =0; p < valuesInWindow.size(); p++){
            dataPointList.add(valuesInWindow.get(p));
            timePointList.add(timesInWindow.get(p)); // Timestamps
        }

        for (int i = 0; i < dataPointList.size(); i++) {
            if (i != 0 && i < dataPointList.size() - 1) {
                forwardSlope = dataPointList.get(i + 1) - dataPointList.get(i);
                downwardSlope = dataPointList.get(i)- dataPointList.get(i - 1);
                if (forwardSlope < 0 && downwardSlope > 0 && dataPointList.get(i) > stepThreshold ) {
                    // Update the number of steps
                    stepsCompleted += 1;
                    Log.d("ACCELEROMETER STEPS: ", String.valueOf(stepsCompleted));
                    // Update the TextView
                    updateTextView();
                    // Insert the data in the DB
                    App.getDBInstance().stepDAO().addStep(new Step(day, hour, timePointList.get(i)));
                }
            }
        }
    }

    // Calculate the number of steps from the step detector
    private void countSteps(float step) {
        // Update the number of steps
        stepsCompleted += (int) step;
        Log.d("STEP DETECTOR STEPS: ", String.valueOf(stepsCompleted));
        // Update the TextView
        updateTextView();
        // Insert the data in the DB
        for(int s = 0; s < (int)step; s++) {
            App.getDBInstance().stepDAO().addStep(new Step(day, hour, timestamp));
        }
    }

    private void updateTextView() {
        // Update the TextView
        stepsCountTextView.setText(String.valueOf(
                pth == ProgressTypeHome.KCAL ?
                        Utils.convertStepsToCal(stepsCompleted) :
                        stepsCompleted
        ));
    }
}