package com.toedro.fao.ui;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.toedro.fao.R;
import com.toedro.fao.ui.settings.ProgressTypeHome;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static double convertStepsToCal(int steps) {
        return steps * 0.5;
    }

    public static int progressTypeHomeToId(ProgressTypeHome type) {
        int r = R.id.radio_button_1;
        switch (type) {
            case STEPS:
                r = R.id.radio_button_2;
                break;
            case KCAL:
                r = R.id.radio_button_1;
                break;
        }
        return r;
    }

    public static ProgressTypeHome idToProgressTypeHome(int id) {
        ProgressTypeHome r = ProgressTypeHome.KCAL;
        switch (id) {
            case R.id.radio_button_2:
                r = ProgressTypeHome.STEPS;
                break;
            case R.id.radio_button_1:
                r = ProgressTypeHome.KCAL;
                break;
        }
        return r;
    }

    public static List<String> generateDateIntervals(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate
                        .plusDays(i)
                        .format(dateFormatter))
                .collect(Collectors.toList());
    }
}
