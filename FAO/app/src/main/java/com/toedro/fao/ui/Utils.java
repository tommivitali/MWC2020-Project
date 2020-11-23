package com.toedro.fao.ui;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static double convertStepsToCal(int steps) {
        return steps * 0.5;
    }
}
