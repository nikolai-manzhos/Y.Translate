package com.defaultapps.translator.ui.lang;

import com.defaultapps.translator.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class LanguageSelectionPresenterImpl extends BasePresenter<LanguageSelectionView> implements LanguageSelectionPresenter {

    @Inject
    public LanguageSelectionPresenterImpl(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }
}
