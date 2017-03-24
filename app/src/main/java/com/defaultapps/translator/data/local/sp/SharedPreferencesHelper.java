package com.defaultapps.translator.data.local.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.defaultapps.translator.di.ApplicationContext;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    private final String SHARED_PREFERENCES_NAME = "com.defaultapps.blueprint.SP";

    @Inject
    public SharedPreferencesHelper(@ApplicationContext Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean(key, value).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putLong(key, value).apply();
    }

    public Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putStringSet(key, value).apply();
    }
}
