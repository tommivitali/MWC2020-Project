package com.toedro.fao.ui.charts;

import android.app.DatePickerDialog;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.StepsQueryResult;
import com.toedro.fao.ui.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
        // Set default date values
        de.getEditText().setText(LocalDate.now().format(DateTimeFormatter.ofPattern(getResources().getString(R.string.date_layout_UI))));
        ds.getEditText().setText(LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern(getResources().getString(R.string.date_layout_UI))));
//        de.getEditText().setText(new SimpleDateFormat(getResources().getString(R.string.date_layout_UI)).format(LocalDate.now()));
//        ds.getEditText().setText(new SimpleDateFormat(getResources().getString(R.string.date_layout_UI)).format(LocalDate.now().minusDays(5)));

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
                SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_layout_UI), Locale.US);
                ds.getEditText().setText(sdf.format(CalendarFrom.getTime()));
                //parse date
                try {
                    From = new SimpleDateFormat(getResources().getString(R.string.date_layout_UI)).parse(ds.getEditText().getText().toString());
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
                de.getEditText().setText(sdf.format(CalendarTo.getTime()));
                //parse date
                try {
                    To = new SimpleDateFormat("dd/MM/yyyy").parse(de.getEditText().getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), To.toString(), Toast.LENGTH_SHORT).show(); //debug
            }
        };

        de.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateTo, CalendarTo
                        .get(Calendar.YEAR), CalendarTo.get(Calendar.MONTH),
                        CalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ds.getEditText().setOnClickListener(new View.OnClickListener() {
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
        //Case unckecked --> cal by default
        List<Integer> ids = materialButtonToggleGroup.getCheckedButtonIds();
        if (ids.size() == 0){
            progressBar.setVisibility(View.GONE);
            materialButtonToggleGroup.check(R.id.toggleCal);
            steps = false;
            Toast.makeText(getContext(), "CAL", Toast.LENGTH_SHORT).show(); //debug
            barChartViewCal.setFitBars(true);
            loadBarData(barChartViewCal);//
            barChartViewCal.getDescription().setText("");
            barChartViewCal.getLegend().setEnabled(false);
            ChartsGraphsLayoutCalories.setVisibility(View.VISIBLE);
            ChartsGraphsLayoutSteps.setVisibility(View.GONE);
        }
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (group.getCheckedButtonId() == R.id.toggleSteps) {
                    progressBar.setVisibility(View.GONE);
                    steps = true;
                    //Toast.makeText(getContext(), "STEPS", Toast.LENGTH_SHORT).show(); //debug
                    barChartViewStep.setFitBars(true);
                    loadBarData(barChartViewStep);//
                    barChartViewStep.getDescription().setText("");
                    barChartViewStep.getLegend().setEnabled(false);
                    //Cartesian cartesian = createColumnChart(); //redo graph
                    //anyChartViewStep.setChart(cartesian);
                    ChartsGraphsLayoutCalories.setVisibility(View.GONE);
                    ChartsGraphsLayoutSteps.setVisibility(View.VISIBLE);
                } else {// if (group.getCheckedButtonId() == R.id.toggleCal){
                    progressBar.setVisibility(View.GONE);
                    //Place code related to Cal button
                    steps = false;
                   // Toast.makeText(getContext(), "CAL", Toast.LENGTH_SHORT).show(); //debug
                    barChartViewCal.setFitBars(true);
                    loadBarData(barChartViewCal);//
                    barChartViewCal.getDescription().setText("");
                    barChartViewCal.getLegend().setEnabled(false);
                    ChartsGraphsLayoutCalories.setVisibility(View.VISIBLE);
                    ChartsGraphsLayoutSteps.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    public void loadBarData(BarChart chart){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getResources().getString(R.string.date_layout_UI));

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

        //stepsByDay = App.getDBInstance().stepDAO().getSteps(); // se passi i giorni restituisce direttamente solo i dati corretti

        stepsByDay = App.getDBInstance().stepDAO().getSteps(
                Utils.generateDateIntervals(
                        LocalDate.parse(ds.getEditText().getText(), dateFormatter),
                        LocalDate.parse(de.getEditText().getText(), dateFormatter)
                ));

        // If the interval is empty
        if(stepsByDay.size() == 1 && stepsByDay.get(0).getSteps() == 0) {
            Snackbar.make(getView(), R.string.charts_error, Snackbar.LENGTH_LONG).show();
            chart.setData(null);
            //chart.invalidate();
            return;
        }

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