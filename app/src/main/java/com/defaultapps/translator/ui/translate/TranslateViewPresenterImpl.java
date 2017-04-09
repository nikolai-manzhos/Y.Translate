package com.defaultapps.translator.ui.translate;

import android.util.Log;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;

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
    public String getCurrentText() {
        return translateViewInteractor.provideCurrentText();
    }

    @Override
    public void setCurrentText(String text) {
        translateViewInteractor.setCurrentText(text);
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
                        translateResponse -> {
                            Log.d("RESPONSE", translateResponse.getTranslatedText());
                            if (getView() != null) {
                                getView().hideLoading();
                                getView().hideError();
                                getView().showResult(translateResponse.getTranslatedText());
                            }
                        },
                        err -> {
                            if (getView() != null) {
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

    @Override
    public void requestLangNames() {
        getCompositeDisposable().add(
                translateViewInteractor.provideLangNames()
                .subscribe(
                        langList -> {
                            Log.d("TRANSLATE_PRESENTER", langList.toString());
                            if (getView() != null) {
                                getView().setLangNames(langList.get(0), langList.get(1));
                            }
                        },
                        err -> {}
                )
        );
    }

    @Override
    public void checkFirstTimeUser() {
        translateViewInteractor.checkFirstTimeUser();
    }
}
