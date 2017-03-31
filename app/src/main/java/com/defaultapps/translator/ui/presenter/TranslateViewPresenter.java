package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.ui.base.MvpPresenter;
import com.defaultapps.translator.ui.fragment.TranslateView;

public interface TranslateViewPresenter extends MvpPresenter<TranslateView> {
    void setCurrentText(String text);
    void requestTranslation(boolean forceUpdate);
    String getCurrentText();
}
