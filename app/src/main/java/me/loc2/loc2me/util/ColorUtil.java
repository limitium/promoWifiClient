package me.loc2.loc2me.util;

import android.graphics.Color;

public class ColorUtil {
    public static int darker(int color,float k) {
        float[] hsv = getHSV(color);
        hsv[2] *= k; // value component
        return Color.HSVToColor(hsv);
    }

    public static float[] getHSV(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }
}
