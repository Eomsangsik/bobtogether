package com.yesjam.bobtogether.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataPreferences {

    public static final String MyPREFERENCES = "UserDataPref";
    private SharedPreferences sharedPreferences;

    public UserDataPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setEmail(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", value);
        editor.commit();
    }

    public void setFName(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fName", value);
        editor.commit();
    }

    public void setGName(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gName", value);
        editor.commit();
    }

    public void setPicUrl(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("picUrl", value);
        editor.commit();
    }

    public void setPhone(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", value);
        editor.commit();
    }

    public void setGoogleIdToken(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("googleIdToken", value);
        editor.commit();
    }

    public String get(String key){
        String profile = sharedPreferences.getString(key,"fail");
        return profile;
    }

    public String getEmail(){
        String profile = sharedPreferences.getString("email","fail");
        return profile;
    }

    public String getFName(){
        String profile = sharedPreferences.getString("fName","fail");
        return profile;
    }

    public String getGName(){
        String profile = sharedPreferences.getString("gName","fail");
        return profile;
    }

    public String getPicUrl(){
        String profile = sharedPreferences.getString("picUrl","fail");
        return profile;
    }

    public String getAddr(){
        String profile = sharedPreferences.getString("addr","fail");
        return profile;
    }

    public String getPhone(){
        String profile = sharedPreferences.getString("phone","fail");
        return profile;
    }

    public String getGoogleIdToken(){
        String profile = sharedPreferences.getString("googleIdToken","fail");
        return profile;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean hasItem(String key){
        String profile = sharedPreferences.getString(key,"fail");
        if(profile.equals("fail")){
            return false;
        }
        else{
            return true;
        }
    }

}
