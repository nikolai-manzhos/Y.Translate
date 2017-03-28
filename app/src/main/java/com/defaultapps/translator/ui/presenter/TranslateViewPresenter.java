package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.ui.base.MvpPresenter;
import com.defaultapps.translator.ui.fragment.TranslateView;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;

public interface TranslateViewPresenter extends MvpPresenter<TranslateViewImpl> {
    void setCurrentText(String text);
    String getCurrentText();
    void setCurrentLanguage(String languagePair);
    void requestTranslation(boolean forceUpdate);
}
