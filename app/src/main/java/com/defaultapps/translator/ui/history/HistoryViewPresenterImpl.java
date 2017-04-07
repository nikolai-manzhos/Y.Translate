package com.defaultapps.translator.ui.history;


import android.util.Log;

import com.defaultapps.translator.data.interactor.HistoryViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.history.HistoryView;
import com.defaultapps.translator.ui.history.HistoryViewPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class HistoryViewPresenterImpl extends BasePresenter<HistoryView> implements HistoryViewPresenter {

    private HistoryViewInteractor historyViewInteractor;

    @Inject
    public HistoryViewPresenterImpl(CompositeDisposable compositeDisposable,
                                    HistoryViewInteractor historyViewInteractor) {
        super(compositeDisposable);
        this.historyViewInteractor = historyViewInteractor;
    }

    @Override
    public void requestHistoryItems() {
        getCompositeDisposable().add(
                historyViewInteractor.provideHistoryData()
                .subscribe(
                        realmTranslates -> {
                            getView().receiveResult(realmTranslates);
                        },
                        err -> Log.d("REALM", err.toString())
                )
        );
    }

    @Override
    public void addToFav(RealmTranslate realmModel) {
        getCompositeDisposable().add(
                historyViewInteractor.addToFavorite(realmModel)
                .subscribe(


                )
        );
    }

    @Override
    public void deleteFromFav(RealmTranslate realmModel) {
        getCompositeDisposable().add(
                historyViewInteractor.deleteFromFavorite(realmModel)
                .subscribe()
        );
    }
}
