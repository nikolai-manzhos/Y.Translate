package com.defaultapps.translator.ui.favorite;


import com.defaultapps.translator.data.interactor.FavoritesViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.favorite.FavoritesView;
import com.defaultapps.translator.ui.favorite.FavoritesViewPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class FavoritesViewPresenterImpl extends BasePresenter<FavoritesView> implements FavoritesViewPresenter {

    private FavoritesViewInteractor favoritesViewInteractor;

    @Inject
    public FavoritesViewPresenterImpl(CompositeDisposable compositeDisposable,
                                      FavoritesViewInteractor favoritesViewInteractor) {
        super(compositeDisposable);
        this.favoritesViewInteractor = favoritesViewInteractor;
    }

    @Override
    public void requestFavoriteItems() {
        getCompositeDisposable().add(
                favoritesViewInteractor.provideFavoritesData()
                .subscribe(
                        realmData -> {
                            if (getView() != null) {
                                getView().receiveResult(realmData);
                                //TODO: Show NoData view.
                            }
                        },
                        err -> {}
                )
        );
    }

    @Override
    public void deleteFavorites() {
        favoritesViewInteractor.deleteFavoritesData();
    }
}
