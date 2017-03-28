package com.defaultapps.translator.ui.presenter;

import android.util.Log;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.ui.fragment.TranslateView;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


@PerActivity
public class TranslateViewPresenterImpl extends BasePresenter<TranslateViewImpl> implements TranslateViewPresenter {

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
                        translateResponse -> {
                            Log.d("RESPONSE", translateResponse.getText().toString());
                            if (getView() != null) {
                                getView().hideLoading();
                                getView().hideError();
                                getView().showResult(translateResponse.getText().toString());
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
}
