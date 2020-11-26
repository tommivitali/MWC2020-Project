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

    BarChart barChartViewStep, barChartViewCal;

    LinearLayout ChartsGraphsLayoutSteps, ChartsGraphsLayoutCalories; //TEST
    MaterialButtonToggleGroup materialButtonToggleGroup;

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

        ChartsGraphsLayoutCalories.setVisibility(View.GONE);
        ChartsGraphsLayoutSteps.setVisibility(View.INVISIBLE);

        //DATEPICKERS  /////////////////////////////////////////////////////////////////////////////
        //TODO: default TO = data di oggi , constrain: sempre <= oggi
        //TODO: default From = data di 7 giorni fa, constrain: sempre <= TO
        ds = root.findViewById(R.id.chartsTextFieldDateStart); //From
        de = root.findViewById(R.id.chartsTextFieldDateEnd); //To
        // Set default date values
        de.getEditText().setText(LocalDate.now().format(DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI))));
        ds.getEditText().setText(LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI))));

        final Calendar CalendarFrom = Calendar.getInstance(), CalendarTo = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                CalendarFrom.set(Calendar.YEAR, year);
                CalendarFrom.set(Calendar.MONTH, monthOfYear);
                CalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_layout_UI), Locale.US);
                ds.getEditText().setText(sdf.format(CalendarFrom.getTime()));
                try {
                    From = new SimpleDateFormat(getString(R.string.date_layout_UI))
                            .parse(ds.getEditText().getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                CalendarTo.set(Calendar.YEAR, year);
                CalendarTo.set(Calendar.MONTH, monthOfYear);
                CalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_layout_UI), Locale.US);
                de.getEditText().setText(sdf.format(CalendarTo.getTime()));
                try {
                    To = new SimpleDateFormat(getString(R.string.date_layout_UI))
                            .parse(de.getEditText().getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        if (ids.size() == 0) {
            materialButtonToggleGroup.check(R.id.toggleCal);
            loadBarData(barChartViewCal);//
            barChartViewCal.setFitBars(true);
            barChartViewCal.getDescription().setText("");
            barChartViewCal.getLegend().setEnabled(false);
            ChartsGraphsLayoutCalories.setVisibility(View.VISIBLE);
            ChartsGraphsLayoutSteps.setVisibility(View.GONE);
        }

        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (group.getCheckedButtonId() == R.id.toggleSteps) {
                    loadBarData(barChartViewStep);
                    barChartViewStep.setFitBars(true);
                    barChartViewStep.getDescription().setText("");
                    barChartViewStep.getLegend().setEnabled(false);
                    ChartsGraphsLayoutCalories.setVisibility(View.GONE);
                    ChartsGraphsLayoutSteps.setVisibility(View.VISIBLE);
                } else {
                    loadBarData(barChartViewCal);
                    barChartViewCal.setFitBars(true);
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI));
        ArrayList<String> xVals = new ArrayList<String>(); // FINAL
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        // Graphical setup of the chart
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setGranularity(1f);
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        chart.getAxisLeft().setSpaceTop(15f);
        chart.getAxisLeft().setAxisMinimum(0f);

        // Get data from the DB
        stepsByDay = App.getDBInstance().stepDAO().getSteps(
                Utils.generateDateIntervals(
                        LocalDate.parse(ds.getEditText().getText(), dateFormatter),
                        LocalDate.parse(de.getEditText().getText(), dateFormatter)
                ));

        // If the query returns no data
        if(stepsByDay.size() == 1 && stepsByDay.get(0).getSteps() == 0) {
            Snackbar.make(getView(), R.string.charts_error, Snackbar.LENGTH_LONG).show();
            chart.setData(null);
            return;
        }

        int i = 0;
        for (StepsQueryResult entry : stepsByDay) {
            BarEntry val = new BarEntry(i++, Float.valueOf(
                    materialButtonToggleGroup.getCheckedButtonId() == R.id.toggleSteps ?
                            entry.getSteps().toString() :
                            String.valueOf(Utils.convertStepsToCal(entry.getSteps()))
            ));
            xVals.add(entry.getDay());
            yVals.add(val);
        }

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        BarDataSet barDataSet = new BarDataSet(yVals, getString(R.string.charts_label_plot));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        ArrayList<IBarDataSet> dataSet = new ArrayList<IBarDataSet>();
        dataSet.add(barDataSet);

        BarData barData = new BarData(dataSet);
        barData.setValueTextSize(10f);
        barData.setBarWidth(0.9f);

        chart.setData(barData);
    }
}