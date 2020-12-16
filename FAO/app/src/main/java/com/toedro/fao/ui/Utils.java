package com.toedro.fao.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.util.DisplayMetrics;

import com.toedro.fao.Preferences;
import com.toedro.fao.R;
import com.toedro.fao.ui.settings.ChoiceTypeSettings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The Utils class is a class storing methods from other classes to use
 */
public class Utils {
    /**
     * @param dp
     * @return dp converted to pixels
     */
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * @param steps: number of steps taken
     * @param activity
     * @param context
     * @return Rounded to 2 decimal places the result of the formula for calculate the calories burned walking
     * I have used a lot of research in this one: various formulas and ways to calculate this conversion; we decided to use less
     * possible data from the user: just height and weight.
     * The cohoosen formula in the end is based on Arcelli's formula: Grams of consumed fat = (km x kg of body weight)/20, on average pace ~= 3km/h
     * after discarding this //https://calculator.academy/steps-to-calories-calculator/ and METs requred formulas
     * (//MET, vVo2Max, VO2Max, KCal/min: MET = vVO2Max = VO2Max / 3.5 ~= kCalBurnt / (bodyMassKg * timePerformingHours) Kcal/Min
     *         ~= 5 * bodyMassKg * VO2 / 1000; VO2 ~= (currentHeartRate / MaxHeartRate) * VO2Max; MaxHeartRate ~= 210 - (0.8 * ageYears)),
     * https://lowellrunning.com/stepspermile/ tells you how to convert height in stride lenght at pace: average stride length / height = 0.413 (all in inches| 1in = 2.54cm; 1cm = 0,3937in)
     * and considered that on average humans burn 55cal(Cal?)/km, 1km every 1243 steps
     */
    public static double convertStepsToCal(int steps, Activity activity, Context context) {
        double height = Preferences.getHeight(activity, context);
        int weight = Preferences.getWeight(activity, context);
        //formula Arcelli in Cal(kcal) //((height * 0.3937 * 0.413) = step in inches) *0.0254 = step in meters)/1000) * weight*const = kcal for step
        double cal = 0.5 * weight * (steps * (height * 0.3937 * 0.413)*0.0254) /1000; //steps * 0.4 * BMI * speed;
        return Math.round(cal * 100) / 100.0;
    }
    /**
     *
     * @param activity
     * @param context
     * @return returns rounded Basal Metabolic Rate: kcal(Cal) burned during 24h by doing nothing.
     * The calculation is done by using the most common formula, requiring only user's age, sex, weight and height
     * more info https://www.youmath.it/matematica-e-benessere/3779-calcolo-metabolismo-basale.html and https://www.calculator.net/bmr-calculator.html
     */
    public static double calculateBMR(Activity activity, Context context){
        int age =  Preferences.getAge(activity, context);
        boolean male = (Preferences.getSex(activity, context).equals("Male"));
        int sex = male ? 5 : -161;   //costante basata sul sesso dell'individuo
        int height = Preferences.getHeight(activity, context);
        int weight = Preferences.getWeight(activity, context);
        return (10 * weight) + (6.25 * height) - (5 * age) + sex;
    }
    /**
     *
     * @param activity
     * @param context
     * @return calories consumed just by user's BMR today (from 00:00 to now of the same day)
     */
    public static double calculateDailyBMR(Activity activity, Context context) {
        double bmr = calculateBMR(activity, context);

        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long seconds = (now - c.getTimeInMillis()) / 1000;

        return bmr / (24 * 60 * 60) * seconds;
    }
    /**
     *
     * @param type
     * @return converted ChoiceTypeSettings to respective id
     */
    public static int ChoiceTypeSettingsToId(ChoiceTypeSettings type) {
        int r = R.id.choice_unchanged;
        switch (type) {
            case DIMAGRIRE:
                r = R.id.choice_dimagrire;
                break;
            case INGRASSARE:
                r = R.id.choice_ingrassare;
                break;
            case MANTENERE:
                r = R.id.choice_unchanged;
                break;
        }
        return r;
    }
    /**
     *
     * @param id
     * @return converted id to ChoiceTypeSettings
     */
    public static ChoiceTypeSettings idToChoiceTypeSettings(int id) {
        ChoiceTypeSettings r = ChoiceTypeSettings.MANTENERE;
        switch (id) {
            case R.id.choice_dimagrire:
                r = ChoiceTypeSettings.DIMAGRIRE;
                break;
            case R.id.choice_ingrassare:
                r = ChoiceTypeSettings.INGRASSARE;
                break;
            case R.id.choice_unchanged:
                r = ChoiceTypeSettings.MANTENERE;
                break;
        }
        return r;
    }

    /**
     *
     * @param type
     * @return converted ChoiceTypeSettings to id
     */
    public static int progressTypeHomeToId(ChoiceTypeSettings type) {
        int r = R.id.choice_unchanged;
        switch (type) {
            case DIMAGRIRE:
                r = R.id.choice_dimagrire;
                break;
            case INGRASSARE:
                r = R.id.choice_ingrassare;
                break;
            case MANTENERE:
                r = R.id.choice_unchanged;
                break;
        }
        return r;
    }
    /**
     * @param startDate
     * @param endDate
     * @return
     */
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
