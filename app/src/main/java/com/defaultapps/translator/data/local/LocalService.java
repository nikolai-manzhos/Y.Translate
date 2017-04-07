package com.defaultapps.translator.data.local;

import android.util.Log;

import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.model.realm.RealmTranslate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

@Singleton
public class LocalService {

    private SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public LocalService(SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
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

    private RealmTranslate findInRealm(Realm realm, String text, String languagePair) {
        return realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findFirst();
    }

    private void deleteFromRealm(Realm realm, String text, String languagePair) {
        RealmResults<RealmTranslate> row = realm.where(RealmTranslate.class).equalTo("text", text).equalTo("languageSet", languagePair).findAll();
        row.deleteAllFromRealm();
    }
}
