package com.defaultapps.translator.data.local;

import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.model.realm.RealmTranslate;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;

@Singleton
public class LocalService {

    private SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public LocalService(SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
    }


    public String writeToRealm(TranslateResponse translateResponse) {
        Realm realm = Realm.getDefaultInstance();
        String currentText = sharedPreferencesManager.getCurrentText();
        realm.executeTransaction(transactionRealm -> {
            RealmTranslate realmTranslate = findInRealm(realm, currentText);
            if (realmTranslate != null) {
                deleteFromRealm(realm, currentText);
            }
            realmTranslate = transactionRealm.createObject(RealmTranslate.class, currentText);
            realmTranslate.setFavorite(false);
            realmTranslate.setLanguageSet(translateResponse.getLang());
            realmTranslate.setTranslatedText(translateResponse.getText().get(0));
        });
        realm.close();
        return currentText;
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


    private RealmTranslate findInRealm(Realm realm, String text) {
        return realm.where(RealmTranslate.class).equalTo("text", text).findFirst();
    }

    private void deleteFromRealm(Realm realm, String text) {
        RealmResults<RealmTranslate> row = realm.where(RealmTranslate.class).equalTo("text", text).findAll();
        row.deleteAllFromRealm();
    }
}
