package com.toedro.fao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.toedro.fao.ui.settings.ProgressTypeHome;

public class Preferences {
    public static String getLanguage(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultLanguageValue = context.getString(R.string.saved_language_default_key);
        String languageShared = sharedPref.getString(context.getString(R.string.saved_language_saved_key), defaultLanguageValue);
        return languageShared;
    }

    public static ProgressTypeHome getProgressTypeHome(Activity activity, Context context) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultHomeProgressTypeValue = context.getString(R.string.saved_home_progress_type_default_key);
        String homeProgressTypeShared = sharedPref.getString(context.getString(R.string.saved_home_progress_type_saved_key), defaultHomeProgressTypeValue);
        return ProgressTypeHome.valueOf(homeProgressTypeShared);
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
}
