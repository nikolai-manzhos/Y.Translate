package com.defaultapps.translator.ui.lang;

import com.defaultapps.translator.ui.base.MvpPresenter;


public interface LanguageSelectionPresenter extends MvpPresenter<LanguageSelectionView> {
    void requestLangList();
    void setSourceLang(String sourceLang);
    void setSourceLangName(String sourceLangName);
    void setTargetLang(String targetLang);
    void setTargetLangName(String targetLangName);
}
