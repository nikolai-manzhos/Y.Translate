package com.defaultapps.translator.ui.history;

import com.defaultapps.translator.ui.base.MvpPresenter;
import com.defaultapps.translator.ui.history.HistoryView;


public interface HistoryViewPresenter extends MvpPresenter<HistoryView> {
    void requestHistoryItems();
}
