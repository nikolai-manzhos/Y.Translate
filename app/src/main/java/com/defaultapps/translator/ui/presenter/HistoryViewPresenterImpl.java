package com.defaultapps.translator.ui.presenter;


import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.fragment.HistoryView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class HistoryViewPresenterImpl extends BasePresenter<HistoryView> implements HistoryViewPresenter {

    @Inject
    public HistoryViewPresenterImpl(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }
}
