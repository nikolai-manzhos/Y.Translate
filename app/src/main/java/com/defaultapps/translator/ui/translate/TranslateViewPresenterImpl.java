package com.defaultapps.translator.ui.translate;

import android.util.Log;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


@PerActivity
public class TranslateViewPresenterImpl extends BasePresenter<TranslateView> implements TranslateViewPresenter {

    private TranslateViewInteractor translateViewInteractor;
    private RxBus rxBus;

    @Inject
    public TranslateViewPresenterImpl(TranslateViewInteractor translateViewInteractor,
                                      CompositeDisposable compositeDisposable,
                                      RxBus rxBus) {
        super(compositeDisposable);
        this.translateViewInteractor = translateViewInteractor;
        this.rxBus = rxBus;
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
                                getView().showResult();
                                getView().deliverData(translateResponse);
                                rxBus.publish(Global.HISTORY_UPDATE, true);
                            }
                        },
                        err -> {
                            if (getView() != null) {
                                getView().hideLoading();
                                getView().hideResult();
                                getView().showError();
                            }
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

    @Override
    public void swapLanguages() {
        translateViewInteractor.swapLanguages();
    }

    @Override
    public void addToFavorites(RealmTranslate realmTranslate) {
        getCompositeDisposable().add(
                translateViewInteractor.addToFavorites(realmTranslate)
                .subscribe(
                        result -> {
                            rxBus.publish(Global.HISTORY_UPDATE, result);
                            rxBus.publish(Global.FAVORITES_UPDATE, result);
                        }
                )
        );
    }

    @Override
    public void deleteFromFavorites(RealmTranslate realmInstance) {
        getCompositeDisposable().add(
                translateViewInteractor.removeFromFavorites(realmInstance)
                .subscribe(
                        result -> {
                            rxBus.publish(Global.HISTORY_UPDATE, result);
                            rxBus.publish(Global.FAVORITES_UPDATE, result);
                        }
                )
        );
    }
}
