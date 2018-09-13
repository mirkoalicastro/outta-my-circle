package com.acg.outtamycircle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class Settings {
    public static float volume = 1f;
    public static boolean soundEnabled = true;

    public static final int DKGREEN = Color.parseColor("#0b4d10");
    public static final int DKRED = Color.parseColor("#621219");
    public static final int DKGRAY = Color.parseColor("#171d25");
    public static final int WHITE50ALFA = 0xEBCACACA;
    public static final int INTERNAL_GRADIENT = Color.parseColor("#5a5a5a");
    public static final int EXTERNAL_GRADIENT = Color.parseColor("#060606");

    public static void loadSettings(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferences.getBoolean("sound", soundEnabled);
    }

    public static void setSoundEnabled(Activity activity, boolean soundEnabled) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("sound", soundEnabled);
        Settings.soundEnabled = soundEnabled;
        editor.apply();
    }

}
