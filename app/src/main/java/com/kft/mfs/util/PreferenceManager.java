package com.kft.mfs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
    private final SharedPreferences preferences;
    private Editor editor;
    private Context context;

    private static final String PREF_FILE_NAME = "khuchrataka";
    private static PreferenceManager preferenceManager;

    private static final String KEY_NAME = "NAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String KEY_REFRESH_DASHBOARD = "KEY_REFRESH_DASHBOARD";
    private static final String KEY_RESET_PIN = "KEY_RESET_PIN";
    private static final String KEY_TRXREFERENCENUMBER = "KEY_TRXREFERENCENUMBER";
    private static final String KEY_TOKENREFID = "KEY_TOKENREFID";
    private static final String KEY_TOKENTYPE = "KEY_TOKENTYPE";

    private static final String DELIMITER = ":";
    private static final String KEY_APP_START_FIRSTLY = "KEY_APP_START_FIRSTLY";
    private static final String KEY_RUNTIME_PERMISSION_CAMERA = "KEY_RUNTIME_PERMISSION_CAMERA";


    private PreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        //setAppFirtTimeRun(true);
    }

    public static synchronized PreferenceManager getPreferenceManager(Context context) {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager(context);
        }
        return preferenceManager;
    }

    public void setName(String name) {
        editor.putString(KEY_NAME, name).apply();
    }

    public String getName() {
        return preferences.getString(KEY_NAME, null);
    }


    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }


    public void setPhoneNumber(String phoneNumber) {
        editor.putString(KEY_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return preferences.getString(KEY_PHONE_NUMBER, null);
    }

    public void setAppFirtTimeRun(boolean isFirstRun) {
        editor.putBoolean(KEY_APP_START_FIRSTLY, isFirstRun).apply();
    }

    public boolean getIsAppFirtTimeRun() {
        return preferences.getBoolean(KEY_APP_START_FIRSTLY, true);
    }

    public void setResetPIN(boolean isResetPinNeeded) {
        editor.putBoolean(KEY_RESET_PIN, isResetPinNeeded).apply();
    }

    public boolean shouldResetPIN() {
        return preferences.getBoolean(KEY_RESET_PIN, false);
    }

    public void setDashboardRefreshNeeded(boolean isDashboardRefreshNeeded) {
        editor.putBoolean(KEY_REFRESH_DASHBOARD, isDashboardRefreshNeeded).apply();
    }

    public boolean shouldRefreshDashboard() {
        return preferences.getBoolean(KEY_REFRESH_DASHBOARD, false);
    }





}
