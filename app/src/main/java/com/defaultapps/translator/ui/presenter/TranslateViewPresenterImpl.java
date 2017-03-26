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
    public void setCurrentText(String text) {
        translateViewInteractor.setCurrentText(text);
    }

    @Override
    public void setCurrentLanguage(String languagePair) {
        translateViewInteractor.setCurrentLanguage(languagePair);
    }

    @Override
    public void requestTranslation(boolean forceUpdate) {
        if (getView() != null) {
            getView().hideResult();
            getView().hideError();
            getView().showLoading();
        }
        getCompositeDisposable().add(
                translateViewInteractor.requestTranslation(forceUpdate)
                .subscribe(
                        translateResponse ->
                        {Log.d("RESPONSE", translateResponse.getText().toString());
                            if (getView() != null) {
                                getView().hideLoading();
                                getView().hideError();
                                getView().showResult(translateResponse.getText().toString());
                            }
                        },
                        err ->
                        {   if (getView() != null) {
                                getView().hideLoading();
                                getView().hideResult();
                                getView().showError();
                            }
                        },
                        () -> {
                            if (getView() != null)
                                getView().hideLoading();
                        }
                )
        );
    }
}
