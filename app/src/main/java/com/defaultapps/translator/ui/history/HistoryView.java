package com.defaultapps.translator.ui.history;

import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.MvpView;

import java.util.List;


public interface HistoryView extends MvpView {
    void receiveResult(List<RealmTranslate> realmTranslateList);
    void showNoDataView();
    void hideNoDataView();
}
