package com.example.fao.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fao.R;
import com.example.fao.StepAppOpenHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    public Context context;
    MaterialButton StartStop;

    // Text view variables
    public TextView stepsCountTextView;
    public TextView kindOfCountTextView;

    // ACC sensors.
    private Sensor mSensorACC;
    private SensorManager mSensorManager;
    private SensorEventListener listener;

    // Step Detector sensor
    private Sensor mSensorStepDetector;

    //Completed steps
    static int stepsCompleted = 0;

    private HomeViewModel homeViewModel;
    //TODO change this to get 0 = steps, 1 = CAL; based on settings --> bind value with settings
    public int type = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Get the number of steps stored in the current date
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        stepsCountTextView = (TextView) root.findViewById(R.id.stepsCount);
        kindOfCountTextView = (TextView) root.findViewById(R.id.kindOfCount);

        // Set the Views with the number of stored steps
        stepsCountTextView.setText(String.valueOf(stepsCompleted));
        if(type == 0)
            kindOfCountTextView.setText(getResources().getString(R.string.unit_measure3) + " done today"); //if steps
        else //1
            kindOfCountTextView.setText(getResources().getString(R.string.unit_measure4) + " burned today"); //if Cal

        stepsCompleted = StepAppOpenHelper.loadSingleRecord(getContext(), fDate);

        //Get the writable database
        StepAppOpenHelper databaseOpenHelper = new StepAppOpenHelper(this.getContext());
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        //  Get an instance of the sensor manager.
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Step detector instance
        mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        listener = new StepCounterListener(database, stepsCountTextView);

        // Toggle button
        StartStop = (MaterialButton) root.findViewById(R.id.buttonStartStopStepcounter);
        StartStop.setChecked(true); //default stop counter
        StartStop.addOnCheckedChangeListener(new MaterialButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialButton button, boolean isChecked) {

                if (button.getId() == R.id.buttonStartStopStepcounter && !isChecked) {

                    //Place code related to Start button
                   // Toast.makeText(getContext(), "START", Toast.LENGTH_SHORT).show();

                    // Check if the Accelerometer sensor exists
                    if (mSensorACC != null) {

                        // Register the ACC listener
                        mSensorManager.registerListener(listener, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Toast.makeText(getContext(), R.string.acc_not_available, Toast.LENGTH_SHORT).show();

                    }

                    // Check if the Step detector sensor exists
                    if (mSensorStepDetector != null) {
                        // Register the ACC listener
                        mSensorManager.registerListener(listener, mSensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Toast.makeText(getContext(), R.string.step_not_available, Toast.LENGTH_SHORT).show();

                    }
                    //change text button
                    StartStop.setText(R.string.home_stop_stepcounter);

                } else if (button.getId() == R.id.buttonStartStopStepcounter && isChecked) {
                    //Place code related to Stop button
                   // Toast.makeText(getContext(), "STOP", Toast.LENGTH_SHORT).show();

                    // Unregister the listener
                    mSensorManager.unregisterListener(listener);

                    //change text button
                    StartStop.setText(R.string.home_start_stop_stepcounter);
                }
            }
        });



        return root;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
        mSensorManager.unregisterListener(listener);
    }

    class StepCounterListener<stepsCompleted> implements SensorEventListener {

        private long lastUpdate = 0;

        public int mACCStepCounter = HomeFragment.stepsCompleted;
        public double ACC_CalCounted = convertCal(mACCStepCounter);

        ArrayList<Integer> mACCSeries = new ArrayList<Integer>();
        ArrayList<String> mTimeSeries = new ArrayList<String>();

        private double accMag = 0d;
        private int lastXPoint = 1;
        int stepThreshold = 10;

        // Android step detector
        public int mAndroidStepCounter = 0;

        TextView stepsCountTextView;

        //variables to graphs
        SQLiteDatabase database;
        public String timestamp;
        public String day;
        public String hour;

        // Constructor, get the database, TextView as args
        public StepCounterListener(SQLiteDatabase db, TextView tv){
            stepsCountTextView = tv;
            database = db;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {

                // Case of the ACC
                case Sensor.TYPE_LINEAR_ACCELERATION:

                    // Get x,y,z
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    //////////////////////////// -- PRINT ACC VALUES -- ////////////////////////////////////
                    // Timestamp
                    long timeInMillis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;

                    // Convert the timestamp to date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                    String date = jdf.format(timeInMillis);

                    // Get the date, the day and the hour
                    timestamp = date;
                    day = date.substring(0,10);
                    hour = date.substring(11,13);

                    ////////////////////////////////////////////////////////////////////////////////////////

                    /// STEP COUNTER ACC ////
                    accMag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

                    //Update the Magnitude series
                    mACCSeries.add((int) accMag);
                    //Update the time series
                    mTimeSeries.add(timestamp);


                    // Calculate ACC peaks and steps
                    peakDetection();

                    break;

                // case Step detector
                case Sensor.TYPE_STEP_DETECTOR:

                    // Calculate the number of steps
                    countSteps(event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //
        }


        public void peakDetection() {
            int windowSize = 20;

            /* Peak detection algorithm derived from: A Step Counter Service for Java-Enabled Devices Using a Built-In Accelerometer, Mladenov et al.
             */
            int highestValX = mACCSeries.size(); // get the length of the series
            if (highestValX - lastXPoint < windowSize) { // if the segment is smaller than the processing window skip it
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
                dataPointList.add(valuesInWindow.get(p)); // ACC Magnitude data points
                timePointList.add(timesInWindow.get(p)); // Timestamps
            }

            for (int i = 0; i < dataPointList.size(); i++) {
                if (i == 0) {
                }
                else if (i < dataPointList.size() - 1) {
                    forwardSlope = dataPointList.get(i + 1) - dataPointList.get(i);
                    downwardSlope = dataPointList.get(i)- dataPointList.get(i - 1);

                    if (forwardSlope < 0 && downwardSlope > 0 && dataPointList.get(i) > stepThreshold ) {

                        // Update the number of steps
                        mACCStepCounter += 1;
                        Log.d("ACC STEPS: ", String.valueOf(mACCStepCounter));

                        // Update the TextView
                        if(type == 0) {
                            stepsCountTextView.setText(String.valueOf(mACCStepCounter));
                            kindOfCountTextView.setText(getResources().getString(R.string.unit_measure3) + " done today"); //if steps
                        }else { //1
                            stepsCountTextView.setText(String.valueOf(ACC_CalCounted));
                            kindOfCountTextView.setText(getResources().getString(R.string.unit_measure4) + " burned today"); //if Cal
                        }
                        // Insert the data in the database
                        ContentValues values = new ContentValues();
                        values.put(StepAppOpenHelper.KEY_TIMESTAMP, timePointList.get(i));
                        values.put(StepAppOpenHelper.KEY_DAY, day);
                        values.put(StepAppOpenHelper.KEY_HOUR, hour);

                        database.insert(StepAppOpenHelper.TABLE_NAME, null, values);
                    }

                }
            }
        }
//TODO delete this and replace everything from database
        private double convertCal(int steps) {
            //TODO get correct equation
            return steps*0.5;
        }


        // Calculate the number of steps from the step detector
        private void countSteps(float step) {

            //Step count
            mAndroidStepCounter += (int) step;
            Log.d("NUM STEPS ANDROID", "Num.steps: " + String.valueOf(mAndroidStepCounter));
        }
    }
}
