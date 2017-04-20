package com.defaultapps.translator.data.local;

import android.content.Context;
import android.util.Log;

import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.scope.PerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocalService {

    private SharedPreferencesManager sharedPreferencesManager;
    private Context applicationContext;

    public LocalService(SharedPreferencesManager sharedPreferencesManager,
                        @ApplicationContext Context context) {
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.applicationContext = context;
    }

    public void writeToRealm(TranslateResponse translateResponse) {
        Realm realm = Realm.getDefaultInstance();
        Log.d("WriteToRealm", Thread.currentThread().getName());
        String currentText = sharedPreferencesManager.getCurrentText();
        String languagePair = sharedPreferencesManager.getSourceLanguage() + "-" + sharedPreferencesManager.getTargetLanguage();
        realm.executeTransaction(transactionRealm -> {
            RealmTranslate realmTranslate = findInRealm(realm, currentText, languagePair);
            if (realmTranslate != null) {
                deleteFromRealm(realm, currentText, languagePair);
            }
            realmTranslate = transactionRealm.createObject(RealmTranslate.class, currentText + translateResponse.getLang());
            realmTranslate.setHistory(true);
            realmTranslate.setFavorite(false);
            realmTranslate.setLanguageSet(translateResponse.getLang());
            realmTranslate.setText(currentText);
            realmTranslate.setTranslatedText(translateResponse.getText().get(0));
            realmTranslate.setSourceLangName(sharedPreferencesManager.getSourceLanguageName());
            realmTranslate.setTargetLangName(sharedPreferencesManager.getTargetLanguageName());
        });
        realm.close();
    }

    public void rewriteRealmEntry(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            deleteFromRealm(realm, realmTranslate.getText(), realmTranslate.getLanguageSet());
            realmTranslate.setHistory(true);
            transactionRealm.copyToRealm(realmTranslate);
        });
        realm.close();
    }

    public void setCurrentText(String text) {
        sharedPreferencesManager.setCurrentText(text);
    }

    public String getCurrentText() {
        return sharedPreferencesManager.getCurrentText();
    }

    public void setSourceLang(String sourceLang) {
        sharedPreferencesManager.setSourceLanguage(sourceLang);
    }

    public void setSourceLangName(String sourceLangName) {
        sharedPreferencesManager.setSourceLanguageName(sourceLangName);
    }

    public void setTargetLang(String targetLang) {
        sharedPreferencesManager.setTargetLanguage(targetLang);
    }

    public void setTargetLangName(String targetLangName) {
        sharedPreferencesManager.setTargetLanguageName(targetLangName);
    }

    public String getCurrentLanguagePair() {
        return sharedPreferencesManager.getSourceLanguage() + "-" + sharedPreferencesManager.getTargetLanguage();
    }

    public List<String> provideLangNames() {
        return Arrays.asList(sharedPreferencesManager.getSourceLanguageName(), sharedPreferencesManager.getTargetLanguageName());
    }

    public void swapLangs() {
        String srcLng = sharedPreferencesManager.getSourceLanguage();
        String srcLngName = sharedPreferencesManager.getSourceLanguageName();
        sharedPreferencesManager.setSourceLanguage(sharedPreferencesManager.getTargetLanguage());
        sharedPreferencesManager.setSourceLanguageName(sharedPreferencesManager.getTargetLanguageName());
        sharedPreferencesManager.setTargetLanguage(srcLng);
        sharedPreferencesManager.setTargetLanguageName(srcLngName);
    }

    public void checkFirstTimeUser() {
        if (!sharedPreferencesManager.getFirstTimeUser()) {
            sharedPreferencesManager.setSourceLanguage("en");
            sharedPreferencesManager.setSourceLanguageName("English");
            sharedPreferencesManager.setTargetLanguage("ru");
            sharedPreferencesManager.setTargetLanguageName("Russian");
            sharedPreferencesManager.setFirstTimeUser(true);
        }
    }

    public RealmTranslate readFromRealm(String text, String languagePair) {
        Log.d("ReadFromRealm", Thread.currentThread().getName());
        Realm realm = Realm.getDefaultInstance();
        RealmTranslate data = realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findFirst();
        if (data != null) {
            RealmTranslate finalData = realm.copyFromRealm(data);
            realm.close();
            return finalData;
        }
        realm.close();
        return new RealmTranslate();
    }

    public RealmTranslate responseToRealm(TranslateResponse translateResponse) {
        if (translateResponse.getText() != null) {
            return new RealmTranslate(sharedPreferencesManager.getCurrentText() + translateResponse.getLang(),
                    sharedPreferencesManager.getCurrentText(),
                    translateResponse.getText().get(0),
                    false,
                    true,
                    sharedPreferencesManager.getSourceLanguage() + "-" + sharedPreferencesManager.getTargetLanguage(),
                    sharedPreferencesManager.getSourceLanguageName(),
                    sharedPreferencesManager.getTargetLanguageName());
        }
        return new RealmTranslate();
    }

    public List<RealmTranslate> provideHistoryDatabase() {
        Realm realm = Realm.getDefaultInstance();
        List<RealmTranslate> finalData;
        RealmResults<RealmTranslate> data = realm.where(RealmTranslate.class).equalTo("history", true).findAll();
        Log.d("ProvideDatabase", Thread.currentThread().getName());
        finalData = realm.copyFromRealm(data);
        realm.close();
        return finalData;
    }

    public boolean wipeHistory() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<RealmTranslate> rows = realm.where(RealmTranslate.class).equalTo("history", true).findAll();
            for (RealmTranslate entity : rows) {
                entity.setHistory(false);
            }
            RealmResults<RealmTranslate> finalRows = realm.where(RealmTranslate.class).equalTo("history", false).equalTo("favorite", false).findAll();
            finalRows.deleteAllFromRealm();
        });
        realm.close();
        return true;
    }

    public List<RealmTranslate> provideFavoritesDatabase() {
        Realm realm = Realm.getDefaultInstance();
        List<RealmTranslate> finalData;
        RealmResults<RealmTranslate> data = realm.where(RealmTranslate.class).equalTo("favorite", true).findAll();
        finalData = realm.copyFromRealm(data);
        realm.close();
        return finalData;
    }

    public boolean wipeFavorites() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<RealmTranslate> rows = realm.where(RealmTranslate.class).equalTo("favorite", true).findAll();
            for (RealmTranslate entity : rows) {
                entity.setFavorite(false);
            }
            RealmResults<RealmTranslate> finalRows = realm.where(RealmTranslate.class).equalTo("favorite", false).equalTo("history", false).findAll();
            finalRows.deleteAllFromRealm();
        });
        realm.close();
        return true;
    }

    public boolean addToFavorite(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realmTranslate.setFavorite(true);
            realm.insertOrUpdate(realmTranslate);
        });
        realm.close();
        return true;
    }

    public boolean deleteFromFavorite(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realmTranslate.setFavorite(false);
            realm.insertOrUpdate(realmTranslate);
        });
        realm.close();
        return true;
    }

    public Map<String, String> readLangFromFile() throws IOException {
        String line;
        StringBuilder jsonString = new StringBuilder();

        InputStream inputStream = applicationContext.getAssets().open("lang.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((line = bufferedReader.readLine()) != null) {
            jsonString.append(line);
        }
        inputStream.close();

        Map<String, String> retMap = new Gson().fromJson(
                jsonString.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        retMap.remove(sharedPreferencesManager.getSourceLanguage());
        retMap.remove(sharedPreferencesManager.getTargetLanguage());
        return retMap;
    }

    public boolean setCurrentParams(RealmTranslate realmInstance) {
        String langPair = realmInstance.getLanguageSet().toLowerCase();
        String sourceLang = langPair.substring(0, langPair.indexOf("-"));
        String targetLang = langPair.substring(langPair.indexOf("-") + 1);

        sharedPreferencesManager.setCurrentText(realmInstance.getText());
        sharedPreferencesManager.setSourceLanguage(sourceLang);
        sharedPreferencesManager.setSourceLanguageName(realmInstance.getSourceLangName());
        sharedPreferencesManager.setTargetLanguage(targetLang);
        sharedPreferencesManager.setTargetLanguageName(realmInstance.getTargetLangName());
        return true;
    }

    public void removeItemFromHistory(RealmTranslate realmInstance) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transaction -> {
            realmInstance.setHistory(false);
            if (!realmInstance.getFavorite()) {
                deleteFromRealm(realm, realmInstance.getText(), realmInstance.getLanguageSet());
            } else {
                realm.insertOrUpdate(realmInstance);
            }
        });
        realm.close();
    }

    private RealmTranslate findInRealm(Realm realm, String text, String languagePair) {
        return realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findFirst();
    }

    private void deleteFromRealm(Realm realm, String text, String languagePair) {
        RealmResults<RealmTranslate> row = realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findAll();
        row.deleteAllFromRealm();
    }
}
