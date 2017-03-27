package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.ui.base.MvpPresenter;
import com.defaultapps.translator.ui.fragment.TranslateView;

public interface TranslateViewPresenter extends MvpPresenter<TranslateView> {
    void setCurrentText(String text);
    String getCurrentText();
    void setCurrentLanguage(String languagePair);
    void requestTranslation(boolean forceUpdate);
}
