package com.company.androidquiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    public static final int DEF_VALUE = -1;

    private static final String KEY_REMAINING_TIME = "KEY_REMAINING_TIME";
    private static final String KEY_IS_TASK_COMPLETED = "KEY_IS_TASK_COMPLETED";

    private static SharedPrefsManager manager = new SharedPrefsManager();
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static SharedPrefsManager getInstance(Context context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            mPrefsEditor = mSharedPrefs.edit();
        }
        return manager;
    }

    public long getRemainingTime() {
        return mSharedPrefs.getLong(KEY_REMAINING_TIME, DEF_VALUE);
    }

    public void putRemainingTime(long remainingTime) {
        mPrefsEditor.putLong(KEY_REMAINING_TIME, remainingTime);
        mPrefsEditor.commit();
    }

    public boolean isTaskCompleted() {
        return mSharedPrefs.getBoolean(KEY_IS_TASK_COMPLETED, false);
    }

    public void setCompleted(boolean isTaskCompleted) {
        mPrefsEditor.putBoolean(KEY_IS_TASK_COMPLETED, isTaskCompleted);
        mPrefsEditor.commit();
    }

    public void reset() {
        mPrefsEditor.clear();
        mPrefsEditor.commit();
    }
}