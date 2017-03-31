package com.defaultapps.translator.data.local.sp;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CURRENT_TEXT = "current_text";
    private final String SOURCE_LANGUAGE = "source_language";
    private final String TARGET_LANGUAGE = "target_language";

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

    public String getSourceLanguage() {
        return sharedPreferencesHelper.getString(SOURCE_LANGUAGE);
    }

    public void setSourceLanguage(String sourceLanguage) {
        sharedPreferencesHelper.putString(SOURCE_LANGUAGE, sourceLanguage);
    }

    public String getTargetLanguage() {
        return sharedPreferencesHelper.getString(TARGET_LANGUAGE);
    }

    public void setTargetLanguage(String targetLanguage) {
        sharedPreferencesHelper.putString(TARGET_LANGUAGE, targetLanguage);
    }
}
