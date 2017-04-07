package com.defaultapps.translator.ui.lang;


import com.defaultapps.translator.ui.base.MvpView;

import java.util.List;
import java.util.Map;

public interface LanguageSelectionView extends MvpView {
    void updateLangList(Map<String, String> langListData);
}
