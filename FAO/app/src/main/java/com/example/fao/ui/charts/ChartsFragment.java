package com.example.fao.ui.charts;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.fao.R;
import com.example.fao.StepAppOpenHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartsFragment extends Fragment {

    public int todaySteps = 0;
    TextView numStepsTextView;
    AnyChartView anyChartViewCal;
    BarChart barChartViewStep;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    //check unit measure to show
    LinearLayout ChartsGraphsLayoutSteps, ChartsGraphsLayoutCalories; //TEST
    MaterialButtonToggleGroup materialButtonToggleGroup;
    private boolean steps = true; //default steps? TODO
    //Cartesian cartesian;

    public Map<Integer, Integer> stepsByHour = null;
    public Map<String, Integer> stepsByDay = null;
    public Map<String, Double> caloriesByDay = null;

    com.google.android.material.textfield.TextInputLayout  ds, de; //From, To
    Date From, To;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_charts, container, false);
        ChartsGraphsLayoutSteps = root.findViewById(R.id.StepsGraphsLayout);
        ChartsGraphsLayoutCalories = root.findViewById(R.id.CalGraphsLayout);
        // Create column chart
        //////TODO create second chart views and switch them
        anyChartViewCal = root.findViewById(R.id.BarCalChart);
        anyChartViewCal.setProgressBar(root.findViewById(R.id.loadingBar));
        barChartViewStep = (BarChart) root.findViewById(R.id.BarStepChart);

        //for let something be seen
        //ChartsGraphsLayoutCalories.setLayoutParams(new LinearLayout.LayoutParams(anyChartViewCal.getWidth(),0));
        ChartsGraphsLayoutCalories.setVisibility(View.GONE);
        ChartsGraphsLayoutSteps.setVisibility(View.INVISIBLE);

        //cartesian = createColumnChart();
        anyChartViewCal.setBackgroundColor("#00000000");
        //anyChartView.setChart(cartesian); //removing this waits for clicker to show chart
        
        //DATEPICKERS  /////////////////////////////////////////////////////////////////////////////
        ds = root.findViewById(R.id.chartsTextFieldDateStart); //From
        de = root.findViewById(R.id.chartsTextFieldDateEnd); //To
        final EditText textFrom = (EditText) ds.getEditText();
        final EditText textTo = (EditText) de.getEditText();

        final Calendar CalendarFrom = Calendar.getInstance(), CalendarTo = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                CalendarFrom.set(Calendar.YEAR, year);
                CalendarFrom.set(Calendar.MONTH, monthOfYear);
                CalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textFrom.setText(sdf.format(CalendarFrom.getTime()));
                //parse date
                try {
                    From = new SimpleDateFormat("dd/MM/yyyy").parse(textTo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), From.toString(), Toast.LENGTH_SHORT).show(); //debug
            }

        };
        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                CalendarTo.set(Calendar.YEAR, year);
                CalendarTo.set(Calendar.MONTH, monthOfYear);
                CalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textTo.setText(sdf.format(CalendarTo.getTime()));
                //parse date
                try {
                    To = new SimpleDateFormat("dd/MM/yyyy").parse(textTo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), To.toString(), Toast.LENGTH_SHORT).show(); //debug
            }

        };
        textTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateTo, CalendarTo
                        .get(Calendar.YEAR), CalendarTo.get(Calendar.MONTH),
                        CalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        textFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateFrom, CalendarFrom
                        .get(Calendar.YEAR), CalendarFrom.get(Calendar.MONTH),
                        CalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//END DATEPICKERS//////////////////////////////////////////////////////////////////////////////////

        materialButtonToggleGroup = (MaterialButtonToggleGroup) root.findViewById(R.id.toggleButtonGroup);
        materialButtonToggleGroup.setSelectionRequired(true);
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (group.getCheckedButtonId() == R.id.toggleSteps) {
                    steps = true;
                    Toast.makeText(getContext(), "STEPS", Toast.LENGTH_SHORT).show(); //debug
                    ///TODO
                    BarData data = loadBarData();

                    barChartViewStep.setFitBars(true);
                    barChartViewStep.setData(data);
                    barChartViewStep.getDescription().setText("");
                    barChartViewStep.getLegend().setEnabled(false);

                    ChartsGraphsLayoutCalories.setVisibility(View.GONE);
                    ChartsGraphsLayoutSteps.setVisibility(View.VISIBLE);
                    barChartViewStep.invalidate();
                } else  {//if (group.getCheckedButtonId() == R.id.toggleCal){
                    //Place code related to Cal button
                    steps = false;
                    Toast.makeText(getContext(), "CAL", Toast.LENGTH_SHORT).show(); //debug
                    Cartesian cartesian = createColumnChart(); //redo graph
                    anyChartViewCal.setChart(cartesian);
                    ChartsGraphsLayoutCalories.setVisibility(View.VISIBLE);
                    ChartsGraphsLayoutSteps.setVisibility(View.GONE);
                } /*else{ //none selected //MAYBE DO IT LATER
                    MaterialButton def = group.findViewById(R.id.toggleCal);
                    Toast.makeText(getContext(), "Can't Uncheck", Toast.LENGTH_SHORT).show(); //debug
                    def.setChecked(true);
                }*/
            }
        });

        return root;
    }

    /**
     * Utility function to create the column chart
     *
     * @return Cartesian: cartesian with column chart and data
     */
    public Cartesian createColumnChart(){
    // Replace the number of steps for each hour in graph_map with the number of steps read from the database
        List<DataEntry> data = loadData();

    //***** Create column chart using AnyChart library *********/
    // 1. Create and get the cartesian coordinate system for column chart
        Cartesian cartesian = AnyChart.column();
        cartesian.autoRedraw(true);
        Column column;

    // 3. Add the data to column chart and get the columns
        column = cartesian.column(data);
        // Add tooltip to the bar charts and modify its properties
        column.tooltip()
                .titleFormat("Day: {%X}")
                .position(Position.RIGHT_TOP)
                .anchor(Anchor.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);
        if(steps) {
            column.tooltip().format("{%Value}{groupsSeparator: } Steps");
        }else {//if(!steps)
            column.tooltip().format("{%Value}{groupsSeparator: } Calories");
        }
    //***** Modify the UI of the chart *********/
        // Change the color of column chart and its border
        column.fill("#1EB980");
        column.stroke("#1EB980");

        // Modify the UI of the Cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.background().fill("#00000000");
        cartesian.yScale().minimum(0);
        if(steps)
            cartesian.yAxis(0).title("Number of steps");
        else
            cartesian.yAxis(0).title("Number of calories");
        cartesian.xAxis(0).title("Day");
        cartesian.animation(true);

        return cartesian;
}
    List<DataEntry> loadData(){
        //***** Read data from SQLiteDatabase *********/
        // Get the map from the database
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext());
        caloriesByDay = StepAppOpenHelper.loadCalByDay(getContext());

        List<DataEntry> data = new ArrayList<>();
        if(steps){
        for (Map.Entry<String, Integer> entry : stepsByDay.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }}else{
        for (Map.Entry<String, Double> entry : caloriesByDay.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }}
        return data;
    }
    public BarData loadBarData(){
        //https://stackoverflow.com/questions/34742180/how-to-convert-string-to-bar-entry-for-mpcharts-in-android
        ArrayList<String> xVals = new ArrayList<String>();//Arrays.asList(bar_graph_names)
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        if(steps) {
            stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext());
            int i = 0;
            for (Map.Entry<String, Integer> entry : stepsByDay.entrySet()) {
                BarEntry val = new BarEntry(Float.valueOf(entry.getValue()), i);
                xVals.add(entry.getKey());
                yVals.add(val);
            }

        }//else{
        //    caloriesByDay = StepAppOpenHelper.loadCalByDay(getContext());
        //}

        String label = "steps";
        BarDataSet dataset = new BarDataSet(yVals, label);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(16f);

        return new BarData(dataset);//new BarData(xVals, dataset);
    }

    /**
     * Utility generate a bitmap from the chart
     * @param view: AnyChartView
     * @return bitmap:
     */
    private Bitmap generateBitmap(View view) {

        // 1. Create a bitmap with same dimensions as view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // 2. Create a canvas using bitmap
        Canvas canvas = new Canvas(bitmap);

        // 3. We need to check if view has background image.
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        // 4. Draw the view on the canvas
        view.draw(canvas);

        Log.v("MainActivity", "Bitmap generated successfully");

        // 5. Final bitmap
        return bitmap;
    }
    /**
     * Utility function to encode and parse the bitmap into a Uri object
     * @param context: Context
     * @param bitmap: Bitmap
     * @return Uri
     */
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                getString(R.string.image_title), null);
        return Uri.parse(path);
    }
}
