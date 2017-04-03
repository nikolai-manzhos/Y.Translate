package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.ui.base.MvpPresenter;
import com.defaultapps.translator.ui.fragment.HistoryView;

import java.util.List;


public interface HistoryViewPresenter extends MvpPresenter<HistoryView> {
    void requestHistoryItems();
}
