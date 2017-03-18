package com.defaultapps.translator.ui.presenter;

import android.util.Log;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.fragment.TranslateView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


@PerActivity
public class TranslateViewPresenterImpl extends BasePresenter<TranslateView> implements TranslateViewPresenter {

    private TranslateViewInteractor translateViewInteractor;

    @Inject
    public TranslateViewPresenterImpl(TranslateViewInteractor translateViewInteractor,
                                      CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
        this.translateViewInteractor = translateViewInteractor;
    }

    @Override
    public void setLanguage(String from, String to) {

    }

    @Override
    public void requestTranslation(String text) {
        getCompositeDisposable().add(
                translateViewInteractor.requestTranslation(text, "en-ru")
                .subscribe(
                        translateResponse ->
                                Log.d("RESPONSE", translateResponse.getText().toString()),
                        err ->
                                Log.d("ERROR", err.toString())
                )
        );
    }
}
