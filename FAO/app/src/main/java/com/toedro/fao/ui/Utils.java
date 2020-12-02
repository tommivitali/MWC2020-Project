package com.toedro.fao.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.toedro.fao.Preferences;
import com.toedro.fao.R;
import com.toedro.fao.ui.settings.ProgressTypeHome;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static double convertStepsToCal(int steps, Activity activity, Context context) {
        //in media si bruciano 55cal(Cal?)/km, 1km every 1243 steps
        //Calories Burned = #steps * .04 * BMI * AgeFactor * Speed //https://calculator.academy/steps-to-calories-calculator/
        //average stride length / height = 0.413 (all in inches| 1in = 2.54cm; 1cm = 0,3937in)
        //Multiply your height in inches by 0.413 to know average walking stride(in) https://lowellrunning.com/stepspermile/
        //if you weigh 175 pounds, the calculation would look like this: 0.57 x 175 = 99.75 calories per mile. https://www.verywellfit.com/walking-calories-and-distance-calculators-3432711
        // the calculation would look like this for a person who burns 87.5 calories per mile and walks a mile in 1,400 steps: 87.5 calories per mile / 1,400 steps per mile = 0.063 calories per step.
        //MET, vVo2Max, VO2Max, KCal/min: MET = vVO2Max = VO2Max / 3.5 ~= kCalBurnt / (bodyMassKg * timePerformingHours) Kcal/Min
        // ~= 5 * bodyMassKg * VO2 / 1000; VO2 ~= (currentHeartRate / MaxHeartRate) * VO2Max; MaxHeartRate ~= 210 - (0.8 * ageYears)
        //calories burned = distance run (kilometres) x weight of runner (kilograms) x 1.036
        //http://www.shapesense.com/fitness-exercise/calculators/running-calorie-burn-calculator.shtml
        //https://www.topendsports.com/weight-loss/energy-met.htm
        //Kcal ~= METS * bodyMassKg * timePerformingHours; METS = costante che per camminata va dalle 1.8 alle 8.8: 1.8=(1km/h); 3.0 = (4.8km/h); 8.8 = (9km/h)
        //kcal = const(0,5)*weight*dist(in km) https://www.projectinvictus.it/wp-content/uploads/2014/05/quanto-si-consuma-a-correre-e-camminare.png
        double height = Preferences.getHeight(activity, context);
        int weight = Preferences.getWeight(activity, context);
        //formula Arcelli in Cal(kcal) //((height * 0.3937 * 0.413) = passo in inches) *0.0254 = passi in metri)/1000) * weight*const = kcal for step
        double cal = 0.5 * weight * (steps * (height * 0.3937 * 0.413)*0.0254) /1000; //steps * 0.4 * BMI * speed;
        // Round to 2 decimal places
        return Math.round(cal * 100) / 100.0;
    }

    /*returns rounded Basal Metabolic Rate:
    kcal(Cal) burned during 24h by doing nothing*/
    private static double calculate_BMR(Activity activity, Context context){
        int age =  Preferences.getAge(activity, context);
        boolean male = (Preferences.getSex(activity, context).equals("Male"));
        int sex = male? 5 : -161;   //costante basata sul sesso dell'individuo
        int height = Preferences.getHeight(activity, context);
        int weight = Preferences.getWeight(activity, context);
        return (10*weight)+(6.25*height)-(5*age)+sex;
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
