package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.network.NetworkService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.SubscriberCompletableObserver;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

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
        Log.d(TAG, "CONSTRUCTOR");
        this.schedulerProvider = schedulerProvider;
        this.networkService = networkService;
        this.localService = localService;
    }

    public Observable<TranslateResponse> requestTranslation(boolean forceUpdate) {
        if (disposable != null && forceUpdate) {
            disposable.dispose();
            memoryCache = new TranslateResponse();
            Log.d(TAG, "FORCE AND !DISPOSABLE");
        }
        if (disposable == null || disposable.isDisposed()) {
            Log.d(TAG, "DISPOSED");
            translateProcessor = ReplayProcessor.create();

            disposable = Observable.concat(memory(), network(localService.getCurrentText(), localService.getCurrentLanguagePair()))
                    .filter(response -> response.getText() != null).first(new TranslateResponse())
                    .subscribe(translateProcessor::onNext);
        }
        return translateProcessor.toObservable();
    }

    public String provideCurrentText() {
        return localService.getCurrentText();
    }

    public void setCurrentText(String text) {
        localService.setCurrentText(text);
    }


    public Observable<TranslateResponse> network(String text, String language) {
        return networkService.getNetworkCall().getTranslation(API_KEY, text, language)
                .map(response -> memoryCache = response)
                .doOnNext(translateResponse -> Observable.just(localService.writeToRealm(translateResponse)).subscribeOn(AndroidSchedulers.mainThread()))
                .doOnComplete(() -> Log.d(TAG, "NETWORK DONE"))
                .onErrorReturn(throwable -> {
                    if (DEBUG) Log.d(TAG, throwable.toString());
                    return new TranslateResponse();})
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<TranslateResponse> memory() {
        return Observable.just(memoryCache);
    }
}
