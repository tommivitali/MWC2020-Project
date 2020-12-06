package com.toedro.fao.ui.charts;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.StepsQueryResult;
import com.toedro.fao.ui.Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ChartsFragment extends Fragment {

    BarChart barChartViewStep;
    LinearLayout ChartsGraphsLayoutSteps;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    com.google.android.material.textfield.TextInputLayout textInputFrom, textInputTo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_charts, container, false);

        // Get objects from layout
        ChartsGraphsLayoutSteps     = (LinearLayout) root.findViewById(R.id.StepsGraphsLayout);
        barChartViewStep            = (BarChart) root.findViewById(R.id.BarStepChart);
        textInputFrom               = (TextInputLayout) root.findViewById(R.id.chartsTextFieldDateStart);
        textInputTo                 = (TextInputLayout) root.findViewById(R.id.chartsTextFieldDateEnd);
        materialButtonToggleGroup   = (MaterialButtonToggleGroup) root.findViewById(R.id.toggleButtonGroup);

        // Set default date values
        LocalDate defaultDateTo     = LocalDate.now();
        LocalDate defaultDateFrom   = LocalDate.now().minusDays(5);
        textInputTo.getEditText().setText(defaultDateTo.format(DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI))));
        textInputFrom.getEditText().setText(defaultDateFrom.format(DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI))));

        Calendar calendarFrom   = Calendar.getInstance();
        // Set initial values for the first time the user click in the from date selection
        /*
        calendarFrom.set(Calendar.YEAR, defaultDateFrom.getYear());
        calendarFrom.set(Calendar.MONTH, defaultDateFrom.getMonthValue());
        calendarFrom.set(Calendar.DAY_OF_MONTH, defaultDateFrom.getDayOfMonth());
        */
        Calendar calendarTo     = Calendar.getInstance();

        List<Integer> checked = materialButtonToggleGroup.getCheckedButtonIds();

        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarFrom.set(Calendar.YEAR, year);
                calendarFrom.set(Calendar.MONTH, monthOfYear);
                calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                generateChart(checked);
            }
            private void updateLabel() {
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_layout_UI), Locale.US);
                textInputFrom.getEditText().setText(sdf.format(calendarFrom.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarTo.set(Calendar.YEAR, year);
                calendarTo.set(Calendar.MONTH, monthOfYear);
                calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                generateChart(checked);
            }
            private void updateLabel() {
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_layout_UI), Locale.US);
                textInputTo.getEditText().setText(sdf.format(calendarTo.getTime()));

                // If the new To date is before the From date
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI));
                LocalDate dateFrom  = LocalDate.parse(textInputFrom.getEditText().getText(), dateFormatter);
                LocalDate dateTo    = LocalDate.parse(textInputTo.getEditText().getText(), dateFormatter);

                if(dateFrom.isAfter(dateTo)) { // Then there is a problem!
                    textInputFrom.getEditText().setText(dateTo.minusDays(5).format(dateFormatter));
                }
            }
        };

        textInputTo.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateTo,
                        calendarTo.get(Calendar.YEAR),
                        calendarTo.get(Calendar.MONTH),
                        calendarTo.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });
        textInputFrom.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateFrom,
                        calendarFrom.get(Calendar.YEAR),
                        calendarFrom.get(Calendar.MONTH),
                        calendarFrom.get(Calendar.DAY_OF_MONTH));
                try {
                    dialog.getDatePicker().setMaxDate(new SimpleDateFormat(getString(R.string.date_layout_UI))
                            .parse(textInputTo.getEditText().getText().toString()).getTime());
                    dialog.show();
                } catch (Exception e) {
                    Log.w(TAG, "Error parsing the TO date.", e);
                }
            }
        });

        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                List<Integer> checked = group.getCheckedButtonIds();
                Log.d("checked", "onButtonChecked: " + checked);
                generateChart(checked);
            }
        });

        // Show only calories by default
        materialButtonToggleGroup.setSelectionRequired(false);
        materialButtonToggleGroup.setSingleSelection(false);
        materialButtonToggleGroup.check(R.id.toggleCal);

        return root;
    }

    private void generateChart(List<Integer> checked) {
        loadBarData(barChartViewStep, checked);
        barChartViewStep.setFitBars(true);
        barChartViewStep.getDescription().setText("");
        barChartViewStep.getLegend().setEnabled(false);
        barChartViewStep.invalidate();
    }

    private void loadBarData(BarChart chart, List<Integer> checked){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getString(R.string.date_layout_UI));
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yValsSteps = new ArrayList<BarEntry>(), yValsCals = new ArrayList<BarEntry>();

        // Graphical setup of the chart
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setGranularity(1f);
        chart.animateX(500);
        //set decently horizontal distance bars (Y axis)
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        chart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        chart.getAxisLeft().setSpaceTop(10f);
        chart.getAxisRight().setSpaceTop(10f);
        chart.getAxisRight().setMaxWidth(2f);
        chart.getAxisLeft().setMaxWidth(2f);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisRight().setLabelCount(chart.getAxisLeft().getLabelCount(), true);
        chart.getAxisLeft().setLabelCount(chart.getAxisRight().getLabelCount(), true);

        // Get data from the DB
        List<StepsQueryResult> stepsByDay = App.getDBInstance().stepDAO().getSteps(
                Utils.generateDateIntervals(
                        LocalDate.parse(textInputFrom.getEditText().getText(), dateFormatter),
                        LocalDate.parse(textInputTo.getEditText().getText(), dateFormatter)));

        // If the query returns no data
        if(stepsByDay.size() == 1 && stepsByDay.get(0).getSteps() == 0) {
            Snackbar.make(getView(), R.string.charts_error, Snackbar.LENGTH_LONG).show();
            chart.setData(null);
            return;
        }

        int i = 0;

        for (StepsQueryResult entry : stepsByDay) {
            xVals.add(entry.getDay());
            if( checked.contains(R.id.toggleSteps) ) {
                BarEntry valStep = new BarEntry(i++,  Float.valueOf(entry.getSteps().toString()));
                yValsSteps.add(valStep);
            }
            if( checked.contains(R.id.toggleCal) ) {
                BarEntry valCals = new BarEntry(i++, Float.valueOf(String.valueOf(Utils.convertStepsToCal(entry.getSteps(), getActivity(), getContext()))));
                yValsCals.add(valCals);
            }
        }

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        BarData barData = new BarData();
        if(yValsSteps.size() > 0) {
            BarDataSet barDataSetSteps = new BarDataSet(yValsSteps, getString(R.string.charts_label_plot));
            barDataSetSteps.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSetSteps.setValueTextColor(Color.BLACK);
            barDataSetSteps.setValueTextSize(16f);
            barData.addDataSet(barDataSetSteps);
        }
        if(yValsCals.size() > 0) {
            BarDataSet barDataSetCals = new BarDataSet(yValsCals, getString(R.string.charts_label_plot));
            barDataSetCals.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSetCals.setValueTextColor(Color.BLACK);
            barDataSetCals.setValueTextSize(16f);
            barData.addDataSet( barDataSetCals);
        }
        barData.setValueTextSize(10f);
        chart.getXAxis().setLabelCount(xVals.size());
        //x axis fix
        float barWidth = 0.92f;
        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        //labels will be centered as long as "(barSpace + barWidth) * n + groupSpace = 1" is satisfied

        chart.setData(barData);
        if( checked.contains(R.id.toggleSteps) && checked.contains(R.id.toggleCal)) {
            barData.setBarWidth(barWidth/2);
            chart.groupBars(0f, groupSpace, barSpace);
            chart.getXAxis().setCenterAxisLabels(true);
            chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * xVals.size());
        }
        else{
            barData.setBarWidth(barWidth);
            chart.getXAxis().setCenterAxisLabels(false);
        }
    }
}