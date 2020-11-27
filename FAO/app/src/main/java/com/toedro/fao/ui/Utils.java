package com.toedro.fao.ui;

import android.app.Activity;
import android.content.Context;
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
import com.toedro.fao.Preferences;

public class Utils {
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static double convertStepsToCal(int steps) {
        double speed = 3.0;

        return steps * 0.4 * speed;
    }
    public static double convertStepsToCal(int steps, Activity activity, Context context) {
        //Calories Burned = #steps * .04 * BMI * AgeFactor * Speed
        int height = Preferences.getHeight(activity, context);
        int weight = Preferences.getWeight(activity, context);
        //BMI= Weight (kg)/ [height (m]^2
        double BMI = weight/Math.pow(height/100,2);
        //The average walking speed of a person is 3.0 mph.
        double speed = 3.0;

        return steps * 0.4 * BMI * speed;
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
