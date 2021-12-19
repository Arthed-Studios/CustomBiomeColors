package me.arthed.custombiomecolors.utils;

public class ColorUtils {

    public static String intToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

}
