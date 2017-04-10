package com.defaultapps.translator.ui.favorite;


import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.MvpView;

import java.util.List;

public interface FavoritesView extends MvpView {
    void receiveResult(List<RealmTranslate> realmTranslateList);
}
