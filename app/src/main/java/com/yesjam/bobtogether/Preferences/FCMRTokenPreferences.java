package com.yesjam.bobtogether.Preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class FCMRTokenPreferences {
    public static final String MyPREFERENCES = "FCMRTokenPref";
    private SharedPreferences sharedPreferences;

    public FCMRTokenPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setFCMToken(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMRToken", value);
        editor.commit();
    }

    public String get(String key) {
        String profile = sharedPreferences.getString(key, "fail");
        return profile;
    }

    public String getFCMRToken() {
        String profile = sharedPreferences.getString("FCMRToken", "fail");
        return profile;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean hasValue(String key) {
        String profile = sharedPreferences.getString(key, "fail");
        if (profile.equals("fail")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasFCMRToken() {
        String profile = sharedPreferences.getString("FCMRToken", "fail");
        if (profile.equals("fail")) {
            return false;
        } else {
            return true;
        }
    }

}

