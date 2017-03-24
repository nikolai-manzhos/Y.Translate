package com.defaultapps.translator.data.local.sp;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CURRENT_TEXT = "current_text";
    private final String CURRENT_LANGUAGE = "current_language";

    @Inject
    public SharedPreferencesManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    public String getCurrentText() {
        return sharedPreferencesHelper.getString(CURRENT_TEXT);
    }

    public void setCurrentText(String text) {
        sharedPreferencesHelper.putString(CURRENT_TEXT, text);
    }

    public String getCurrentLanguage() {
        return sharedPreferencesHelper.getString(CURRENT_LANGUAGE);
    }

    public void setCurrentLanguage(String languagePair) {
        sharedPreferencesHelper.putString(CURRENT_LANGUAGE, languagePair);
    }
}
