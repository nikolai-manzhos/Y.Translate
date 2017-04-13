package com.defaultapps.translator.ui.favorite;


import com.defaultapps.translator.data.interactor.FavoritesViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.favorite.FavoritesView;
import com.defaultapps.translator.ui.favorite.FavoritesViewPresenter;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class FavoritesViewPresenterImpl extends BasePresenter<FavoritesView> implements FavoritesViewPresenter {

    private FavoritesViewInteractor favoritesViewInteractor;
    private RxBus rxBus;

    @Inject
    public FavoritesViewPresenterImpl(CompositeDisposable compositeDisposable,
                                      FavoritesViewInteractor favoritesViewInteractor,
                                      RxBus rxBus) {
        super(compositeDisposable);
        this.favoritesViewInteractor = favoritesViewInteractor;
        this.rxBus = rxBus;
    }

    @Override
    public void requestFavoriteItems() {
        getCompositeDisposable().add(
                favoritesViewInteractor.provideFavoritesData()
                .subscribe(
                        realmData -> {
                            if (getView() != null) {
                                if (realmData.isEmpty()) {
                                    getView().showNoDataView();
                                } else {
                                    getView().hideNoDataView();
                                }
                                getView().receiveResult(realmData);
                            }
                        },
                        err -> {}
                )
        );
    }

    @Override
    public void deleteFavorites() {
        getCompositeDisposable().add(
                favoritesViewInteractor.deleteFavoritesData()
                .subscribe(
                        result -> {
                            if (result) {
                                rxBus.publish(Global.FAVORITES_UPDATE, true);
                                rxBus.publish(Global.HISTORY_UPDATE, true);
                                rxBus.publish(Global.TRANSLATE_UPDATE, true);
                            }
                        }
                )
        );
    }
}
