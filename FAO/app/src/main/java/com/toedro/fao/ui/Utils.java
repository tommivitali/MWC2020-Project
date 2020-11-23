package com.toedro.fao.ui;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.toedro.fao.R;
import com.toedro.fao.ui.settings.ProgressTypeHome;

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
}
