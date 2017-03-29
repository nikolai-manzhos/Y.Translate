package com.defaultapps.translator.ui.presenter;


import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.fragment.FavoritesView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class FavoritesViewPresenterImpl extends BasePresenter<FavoritesView> implements FavoritesViewPresenter {

    @Inject
    public FavoritesViewPresenterImpl(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }
}
