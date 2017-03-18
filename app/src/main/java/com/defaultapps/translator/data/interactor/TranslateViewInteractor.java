package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.network.NetworkService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.ReplayProcessor;


public class TranslateViewInteractor {

    private final String API_KEY = "trnsl.1.1.20170318T100130Z.eb5aab89080c4223.b30022ef0612fabacc605b1e3989f20e3871f17a";

    private final SchedulerProvider schedulerProvider;
    private final NetworkService networkService;
    private final LocalService localServicel;

    private TranslateResponse translateResponse = new TranslateResponse();
    private ReplayProcessor<TranslateResponse> translateProcessor;
    private Disposable disposable;

    @Inject
    public TranslateViewInteractor(
            SchedulerProvider schedulerProvider,
            NetworkService networkService,
            LocalService localService
    ) {
        this.schedulerProvider = schedulerProvider;
        this.networkService = networkService;
        this.localServicel = localService;
    }

    public Observable<TranslateResponse> requestTranslation(String text, String languagePair) {
        if (disposable == null || disposable.isDisposed()) {
            translateProcessor = ReplayProcessor.create();

            disposable = Observable.concat(memory(),network(text, languagePair))
                    .filter(response -> response.getCode() != null).first(new TranslateResponse())
                    .subscribe(translateResponse -> translateProcessor.onNext(translateResponse));
        }
        return translateProcessor.toObservable();
    }

    private Observable<TranslateResponse> network(String text, String language) {
        return networkService.getNetworkCall().getTranslation(API_KEY, text, language)
                .map(response -> translateResponse = response)
                .onErrorReturn(throwable -> {
                    Log.d("Wew", throwable.toString());
                    return new TranslateResponse();})
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<TranslateResponse> memory() {
        return Observable.just(translateResponse);
    }
}
