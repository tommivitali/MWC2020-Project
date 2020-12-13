package com.toedro.fao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Preferences {

    public static String getLanguage(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultLanguageValue = context.getString(R.string.saved_language_default_key);
        String languageShared = sharedPref.getString(context.getString(R.string.saved_language_saved_key), defaultLanguageValue);
        return languageShared;
    }

    public static String getSex(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultSexValue = context.getString(R.string.saved_sex_default_key);
        String sexShared = sharedPref.getString(context.getString(R.string.saved_sex_saved_key), defaultSexValue);
        return sexShared;
    }

    public static Integer getHeight(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        Integer defaultHeightValue = context.getResources().getInteger(R.integer.saved_height_default_key);
        Integer heightShared = sharedPref.getInt(context.getString(R.string.saved_height_saved_key), defaultHeightValue);
        return heightShared;
    }

    public static Integer getWeight(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        Integer defaultWeightValue = context.getResources().getInteger(R.integer.saved_weight_default_key);
        Integer weightShared = sharedPref.getInt(context.getString(R.string.saved_weight_saved_key), defaultWeightValue);
        return weightShared;
    }

    public static Integer getAge(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        Integer defaultAgeValue = context.getResources().getInteger(R.integer.saved_age_default_key);
        Integer ageShared = sharedPref.getInt(context.getString(R.string.saved_age_saved_key), defaultAgeValue);
        return ageShared;
    }
    public static String getCalChoice(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultChoiceProgressTypeValue = context.getString(R.string.saved_choice_progress_type_default_key);
        String choiceProgressType = sharedPref.getString(String.valueOf(R.string.saved_choice_progress_type_saved_key), defaultChoiceProgressTypeValue);
        return choiceProgressType;
    }

    private static Integer getValueNotification(int defaultId, int savedKeyId, Context context, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        Integer defaultNotification = context.getResources().getInteger(defaultId);
        Integer notificationShared = sharedPref.getInt(context.getString(savedKeyId), defaultNotification);

        return notificationShared;
    }

    private static Pair<Integer, Integer> getPairHourMinute(int defaultHourId, int defaultMinuteId,
                                                            int savedKeyHourId, int savedKeyMinuteId,
                                                            Context context, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        Integer defaultHour     = context.getResources().getInteger(defaultHourId);
        Integer defaultMinute   = context.getResources().getInteger(defaultMinuteId);
        Integer hourShared      = sharedPref.getInt(context.getString(savedKeyHourId), defaultHour);
        Integer minuteShared    = sharedPref.getInt(context.getString(savedKeyMinuteId), defaultMinute);

        return new Pair<Integer, Integer>(hourShared, minuteShared);
    }

    public static List<Pair<Integer, Integer>> getNotificationsHours(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        List<Pair<Integer, Integer>> notificationList = new ArrayList<>();

        // 1st notification
        if(getValueNotification(
                R.integer.saved_notification_1_default_key, R.string.saved_notification_1_saved_key,
                context, activity) == 1) {
            notificationList.add(getPairHourMinute(
                    R.integer.saved_hour_1_default_key, R.integer.saved_minute_1_default_key,
                    R.string.saved_hour_1_saved_key, R.string.saved_minute_1_saved_key,
                    context, activity));
        }

        // 2nd notification
        if(getValueNotification(
                R.integer.saved_notification_2_default_key, R.string.saved_notification_2_saved_key,
                context, activity) == 1) {
            notificationList.add(getPairHourMinute(
                    R.integer.saved_hour_2_default_key, R.integer.saved_minute_2_default_key,
                    R.string.saved_hour_2_saved_key, R.string.saved_minute_2_saved_key,
                    context, activity));
        }

        // 3rd notification
        if(getValueNotification(
                R.integer.saved_notification_3_default_key, R.string.saved_notification_3_saved_key,
                context, activity) == 1) {
            notificationList.add(getPairHourMinute(
                    R.integer.saved_hour_3_default_key, R.integer.saved_minute_3_default_key,
                    R.string.saved_hour_3_saved_key, R.string.saved_minute_3_saved_key,
                    context, activity));
        }

        // 4th notification
        if(getValueNotification(
                R.integer.saved_notification_4_default_key, R.string.saved_notification_4_saved_key,
                context, activity) == 1) {
            notificationList.add(getPairHourMinute(
                    R.integer.saved_hour_4_default_key, R.integer.saved_minute_4_default_key,
                    R.string.saved_hour_4_saved_key, R.string.saved_minute_4_saved_key,
                    context, activity));
        }

        // 5th notification
        if(getValueNotification(
                R.integer.saved_notification_5_default_key, R.string.saved_notification_5_saved_key,
                context, activity) == 1) {
            notificationList.add(getPairHourMinute(
                    R.integer.saved_hour_5_default_key, R.integer.saved_minute_5_default_key,
                    R.string.saved_hour_5_saved_key, R.string.saved_minute_5_saved_key,
                    context, activity));
        }

        return notificationList;
    }
}
