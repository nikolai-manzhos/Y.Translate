package com.defaultapps.translator.ui.presenter;


import android.util.Log;

import com.defaultapps.translator.data.interactor.HistoryViewInteractor;
import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.fragment.HistoryView;

import java.util.List;

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
                        err -> Log.d("REALM", "ERROR")
                )
        );
    }
}
