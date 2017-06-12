package com.yesjam.bobtogether.Preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class GeneralPreferences {
    public static final String MyPREFERENCES = "GeneralDataPref";
    private SharedPreferences sharedPreferences;

    public GeneralPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        String profile = sharedPreferences.getString(key, "fail");
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

}

