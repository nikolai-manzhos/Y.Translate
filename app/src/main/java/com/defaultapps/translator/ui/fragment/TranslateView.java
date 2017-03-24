package com.defaultapps.translator.ui.fragment;

import com.defaultapps.translator.ui.base.MvpView;


public interface TranslateView extends MvpView {
    void hideError();
    void showError();
    void hideResult();
    void showResult(String result);
}
