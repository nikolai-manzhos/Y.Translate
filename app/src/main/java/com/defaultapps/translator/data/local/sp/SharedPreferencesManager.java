package com.defaultapps.translator.data.local.sp;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CURRENT_TEXT = "current_text";
    private final String SOURCE_LANGUAGE = "source_language";
    private final String SOURCE_LANGUAGE_NAME = "source_language_name";
    private final String TARGET_LANGUAGE = "target_language";
    private final String TARGET_LANGUAGE_NAME = "target_language_name";

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

    public String getSourceLanguageName() {
        return sharedPreferencesHelper.getString(SOURCE_LANGUAGE_NAME);
    }

    public void setSourceLanguageName(String sourceLanguageName) {
        sharedPreferencesHelper.putString(SOURCE_LANGUAGE_NAME, sourceLanguageName);
    }

    public String getTargetLanguage() {
        return sharedPreferencesHelper.getString(TARGET_LANGUAGE);
    }

    public void setTargetLanguage(String targetLanguage) {
        sharedPreferencesHelper.putString(TARGET_LANGUAGE, targetLanguage);
    }

    public String getTargetLanguageName() {
        return sharedPreferencesHelper.getString(TARGET_LANGUAGE_NAME);
    }

    public void setTargetLanguageName(String targetLanguageName) {
        sharedPreferencesHelper.putString(TARGET_LANGUAGE_NAME, targetLanguageName);
    }
}
