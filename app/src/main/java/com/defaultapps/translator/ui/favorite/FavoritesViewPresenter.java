package com.defaultapps.translator.ui.favorite;

import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.MvpPresenter;

public interface FavoritesViewPresenter extends MvpPresenter<FavoritesView> {
    void requestFavoriteItems();
    void deleteFavorites();
    void selectItem(RealmTranslate realmInstance);
}
