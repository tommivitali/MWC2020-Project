package com.example.fao.ui.charts;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fao.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class ChartsFragment extends Fragment {

    public int todaySteps = 0;
    TextView numStepsTextView;
    AnyChartView anyChartView;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    //check unit measure to show
    MaterialButtonToggleGroup materialButtonToggleGroup;
    private boolean steps = true; //default steps? TODO
    Cartesian cartesian;

    public Map<Integer, Integer> stepsByHour = null;
    public Map<String, Integer> stepsByDay = null;
    public Map<String, Double> caloriesByDay = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_charts, container, false);

        // Create column chart
        anyChartView = root.findViewById(R.id.BarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));

        //cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        //anyChartView.setChart(cartesian); //removing this waits for clicker to show chart
        
        //in fragment_charts com.google.android.material.button.MaterialButtonToggleGroup
        // app:checkedButton="@+id/toggleSteps"

        //TODO make it some kind of datepicker//////////////////////////////////////////////////////
        Date From, To;
        final com.google.android.material.textfield.TextInputLayout  ds = root.findViewById(R.id.chartsTextFieldDateStart); //From
        final com.google.android.material.textfield.TextInputLayout  de = root.findViewById(R.id.chartsTextFieldDateStart); //To

        String date_n = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault()).format(new Date());
        ds.getEditText().setText(date_n);
        //changes
        ds.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //http://yii2ideas.blogspot.com/2020/05/android-edit-text-to-show-dd-mm-yyyy.html

        de.getEditText().setText(date_n);

        ds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());

            }
        });
        de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());
            }
        });
//END TODO//////////////////////////////////////////////////////////////////////////////////////////

        materialButtonToggleGroup = (MaterialButtonToggleGroup) root.findViewById(R.id.toggleButtonGroup);
        materialButtonToggleGroup.setSelectionRequired(true);
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                //anyChartView.invalidate(); //does nothing
                //anyChartView.clear(); //make view unusable
                if (group.getCheckedButtonId() == R.id.toggleSteps) {
                    //Place code related to step button
                    steps = true;
                    Toast.makeText(getContext(), "STEPS", Toast.LENGTH_SHORT).show(); //debug
                    cartesian = createColumnChart();
                    anyChartView.setChart(cartesian);
                } else  if (group.getCheckedButtonId() == R.id.toggleCal){//
                    //Place code related to Cal button
                    steps = false;
                    Toast.makeText(getContext(), "CAL", Toast.LENGTH_SHORT).show(); //debug
                    //cartesian.data(new ArrayList<DataEntry>()); //crashes
                    cartesian = createColumnChart(); //redo graph
                    anyChartView.setChart(cartesian);
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