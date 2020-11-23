package com.toedro.fao.ui.charts;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.toedro.fao.db.StepsQueryResult;
import com.toedro.fao.ui.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartsFragment extends Fragment {

    public int todaySteps = 0;
    TextView numStepsTextView;
    BarChart barChartViewStep, barChartViewCal;
    ProgressBar progressBar;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    //check unit measure to show
    LinearLayout ChartsGraphsLayoutSteps, ChartsGraphsLayoutCalories; //TEST
    MaterialButtonToggleGroup materialButtonToggleGroup;
    private boolean steps = true; //default steps? TODO

    public List<StepsQueryResult> stepsByDay = null;

    com.google.android.material.textfield.TextInputLayout  ds, de; //From, To
    Date From, To;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_charts, container, false);

        ChartsGraphsLayoutSteps = root.findViewById(R.id.StepsGraphsLayout);
        ChartsGraphsLayoutCalories = root.findViewById(R.id.CalGraphsLayout);

        barChartViewCal = root.findViewById(R.id.BarCalChart);
        barChartViewStep = (BarChart) root.findViewById(R.id.BarStepChart);
        progressBar = (ProgressBar)root.findViewById(R.id.loadingBar);

        ChartsGraphsLayoutCalories.setVisibility(View.GONE);
        ChartsGraphsLayoutSteps.setVisibility(View.INVISIBLE);

        //DATEPICKERS  /////////////////////////////////////////////////////////////////////////////
        //TODO: default TO = data di oggi , constrain: sempre <= oggi
        //TODO: default From = data di 7 giorni fa, constrain: sempre <= TO
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
                    From = new SimpleDateFormat("dd/MM/yyyy").parse(textFrom.getText().toString());
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
                    progressBar.setVisibility(View.GONE);
                    steps = true;
                    Toast.makeText(getContext(), "STEPS", Toast.LENGTH_SHORT).show(); //debug
                    barChartViewStep.setFitBars(true);
                    loadBarData(barChartViewStep);//
                    barChartViewStep.getDescription().setText("");
                    barChartViewStep.getLegend().setEnabled(false);
                    //Cartesian cartesian = createColumnChart(); //redo graph
                    //anyChartViewStep.setChart(cartesian);
                    ChartsGraphsLayoutCalories.setVisibility(View.GONE);
                    ChartsGraphsLayoutSteps.setVisibility(View.VISIBLE);
                } else  {//if (group.getCheckedButtonId() == R.id.toggleCal){
                    progressBar.setVisibility(View.GONE);
                    //Place code related to Cal button
                    steps = false;
                    Toast.makeText(getContext(), "CAL", Toast.LENGTH_SHORT).show(); //debug
                    barChartViewCal.setFitBars(true);
                    loadBarData(barChartViewCal);//
                    barChartViewCal.getDescription().setText("");
                    barChartViewCal.getLegend().setEnabled(false);
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

    public void loadBarData(BarChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        final ArrayList<String> xVals = new ArrayList<>();//Arrays.asList(bar_graph_names)
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        stepsByDay = App.getDBInstance().stepDAO().getSteps(); // se passi i giorni restituisce direttamente solo i dati corretti
        int i = 0;
        for (StepsQueryResult entry : stepsByDay) {
            BarEntry val = new BarEntry(i++, Float.valueOf(
                    steps ?
                            entry.getSteps().toString() :
                            String.valueOf(Utils.convertStepsToCal(entry.getSteps()))
            ));
            xVals.add(entry.getDay());
            yVals.add(val);
        }

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        String label = "steps";
        BarDataSet dataset = new BarDataSet(yVals, label);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(16f);
        //return new BarData(dataset);//new BarData(xVals, dataset);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        chart.setData(data);
    }
}