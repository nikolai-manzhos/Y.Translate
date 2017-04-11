package com.defaultapps.translator.ui.history;


import android.util.Log;

import com.defaultapps.translator.data.interactor.HistoryViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.history.HistoryView;
import com.defaultapps.translator.ui.history.HistoryViewPresenter;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class HistoryViewPresenterImpl extends BasePresenter<HistoryView> implements HistoryViewPresenter {

    private HistoryViewInteractor historyViewInteractor;
    private RxBus rxBus;

    @Inject
    public HistoryViewPresenterImpl(CompositeDisposable compositeDisposable,
                                    HistoryViewInteractor historyViewInteractor,
                                    RxBus rxBus) {
        super(compositeDisposable);
        this.historyViewInteractor = historyViewInteractor;
        this.rxBus = rxBus;
    }

    @Override
    public void requestHistoryItems() {
        getCompositeDisposable().add(
                historyViewInteractor.provideHistoryData()
                .subscribe(
                        realmTranslates -> {
                            if (getView() != null) {

                                    getView().receiveResult(realmTranslates);

                                    //TODO: Show NoData view.

                            }
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
                    success -> {
                        if (success) {
                            rxBus.publish(Global.FAVORITES_UPDATE, true);
                        }
                    }

                )
        );
    }

    @Override
    public void deleteFromFav(RealmTranslate realmModel) {
        getCompositeDisposable().add(
                historyViewInteractor.deleteFromFavorite(realmModel)
                .subscribe(
                        success -> {
                            if (success) {
                                rxBus.publish(Global.FAVORITES_UPDATE, true);
                            }
                        }
                )
        );
    }

    @Override
    public void deleteHistoryData() {
        getCompositeDisposable().add(
                historyViewInteractor.deleteHistoryData()
                .subscribe(
                        success -> {
                            if (success) {
                                rxBus.publish(Global.HISTORY_UPDATE, true);
                            }
                        }
                )
        );
    }
}
