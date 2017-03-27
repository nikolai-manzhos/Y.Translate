package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.network.NetworkService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class TranslateViewInteractor {

    private final String API_KEY = "trnsl.1.1.20170318T100130Z.eb5aab89080c4223.b30022ef0612fabacc605b1e3989f20e3871f17a";

    private final String TAG = "TranslateViewInteractor";
    private final boolean DEBUG = true;

    private final SchedulerProvider schedulerProvider;
    private final NetworkService networkService;
    private final LocalService localService;

    private TranslateResponse memoryCache = new TranslateResponse();
    private ReplayProcessor<TranslateResponse> translateProcessor;
    private Disposable disposable;

    @Inject
    public TranslateViewInteractor(
            SchedulerProvider schedulerProvider,
            NetworkService networkService,
            LocalService localService)
    {
        this.schedulerProvider = schedulerProvider;
        this.networkService = networkService;
        this.localService = localService;
    }

    public Observable<TranslateResponse> requestTranslation(boolean forceUpdate) {
        if (disposable != null && forceUpdate) {
            disposable.dispose();
            memoryCache = new TranslateResponse();
        }
        if (disposable == null || disposable.isDisposed()) {
            translateProcessor = ReplayProcessor.create();

            disposable = Observable.concat(memory(), network(localService.getCurrentText(), localService.getCurrentLanguage()))
                    .filter(response -> response.getCode() != null).first(new TranslateResponse())
                    .subscribe(translateResponse -> translateProcessor.onNext(translateResponse));
        }
        return translateProcessor.toObservable();
    }

    public String provideCurrentText() {
        return localService.getCurrentText();
    }

    public void setCurrentText(String text) {
        localService.setCurrentText(text);
    }

    public void setCurrentLanguage(String languagePair) {
        localService.setCurrentLanguage(languagePair);
    }

    public Observable<TranslateResponse> network(String text, String language) {
        return networkService.getNetworkCall().getTranslation(API_KEY, text, language)
                .map(response -> memoryCache = response)
                .doOnNext(translateResponse -> Observable.just(localService.writeToRealm(translateResponse)).subscribeOn(Schedulers.io()).subscribe())
                .onErrorReturn(throwable -> {
                    if (DEBUG) Log.d(TAG, throwable.toString());
                    return new TranslateResponse();})
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<TranslateResponse> memory() {
        return Observable.just(memoryCache);
    }
}
