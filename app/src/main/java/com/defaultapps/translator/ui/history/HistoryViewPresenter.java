package com.defaultapps.translator.ui.history;

import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.MvpPresenter;


public interface HistoryViewPresenter extends MvpPresenter<HistoryView> {
    void requestHistoryItems();
    void addToFav(RealmTranslate realmModel);
    void deleteFromFav(RealmTranslate realmModel);
    void deleteHistoryData();
}
