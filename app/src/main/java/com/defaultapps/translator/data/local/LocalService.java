package com.defaultapps.translator.data.local;

import android.content.Context;
import android.util.Log;

import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;

@Singleton
public class LocalService {

    private SharedPreferencesManager sharedPreferencesManager;
    private Context applicationContext;

    @Inject
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
            realmTranslate = transactionRealm.createObject(RealmTranslate.class, currentText);
            realmTranslate.setFavorite(false);
            realmTranslate.setLanguageSet(translateResponse.getLang());
            realmTranslate.setTranslatedText(translateResponse.getText().get(0));
        });
        realm.close();
    }

    public void rewriteRealmEntry(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            deleteFromRealm(realm, realmTranslate.getText(), realmTranslate.getLanguageSet());
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
            return new RealmTranslate(sharedPreferencesManager.getCurrentText(),
                    translateResponse.getText().get(0),
                    false,
                    sharedPreferencesManager.getSourceLanguage() + "-" + sharedPreferencesManager.getTargetLanguage());
        }
        return new RealmTranslate();
    }

    public List<RealmTranslate> provideWholeDatabase() {
        Realm realm = Realm.getDefaultInstance();
        List<RealmTranslate> finalData = new ArrayList<>();
        RealmResults<RealmTranslate> data = realm.where(RealmTranslate.class).findAll();
        Log.d("ProvideDatabase", Thread.currentThread().getName());
        finalData = realm.copyFromRealm(data);
        realm.close();
        return finalData;
    }

    public boolean addToFavorite(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realmTranslate.setFavorite(true);
        realm.insertOrUpdate(realmTranslate);
        realm.commitTransaction();
        realm.close();
        return true;
    }

    public boolean deleteFromFavorite(RealmTranslate realmTranslate) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realmTranslate.setFavorite(false);
        realm.insertOrUpdate(realmTranslate);
        realm.commitTransaction();
        realm.close();
        return true;
    }

    public Map<String, String> readLangFromFile(String requestParam) throws IOException {
        String line = "";
        StringBuilder jsonString = new StringBuilder();

        InputStream inputStream = applicationContext.getAssets().open("lang.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((line = bufferedReader.readLine()) != null) {
            jsonString.append(line);
        }
        inputStream.close();

        Map<String, String> retMap = new Gson().fromJson(
                jsonString.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        if (requestParam.equals("source")) {
            retMap.remove(sharedPreferencesManager.getSourceLanguage());
        } else if (requestParam.equals("target")) {
            retMap.remove(sharedPreferencesManager.getTargetLanguage());
        }
        return retMap;
    }

    private RealmTranslate findInRealm(Realm realm, String text, String languagePair) {
        return realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findFirst();
    }

    private void deleteFromRealm(Realm realm, String text, String languagePair) {
        RealmResults<RealmTranslate> row = realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findAll();
        row.deleteAllFromRealm();
    }
}
