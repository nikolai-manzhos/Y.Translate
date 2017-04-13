package com.defaultapps.translator.ui.translate;

import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.MvpView;


public interface TranslateView extends MvpView {
    void hideError();
    void showError();
    void hideResult();
    void showResult();
    void deliverData(RealmTranslate realmInstance);
    void setLangNames(String sourceLangName,
                      String targetLangName);
}
