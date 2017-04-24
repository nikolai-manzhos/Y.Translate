package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.TranslateResponse;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.data.network.NetworkService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.ReplayProcessor;

@Singleton
public class TranslateViewInteractor {

    private final String API_KEY = "trnsl.1.1.20170318T100130Z.eb5aab89080c4223.b30022ef0612fabacc605b1e3989f20e3871f17a";

    private final String TAG = "TranslateViewInteractor";

    private final SchedulerProvider schedulerProvider;
    private final NetworkService networkService;
    private final LocalService localService;

    private RealmTranslate memoryCache = new RealmTranslate();
    private ReplayProcessor<RealmTranslate> translateProcessor;
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

    public Observable<RealmTranslate> requestTranslation(boolean forceUpdate) {
        if (disposable != null && forceUpdate) {
            disposable.dispose();
            memoryCache = new RealmTranslate();
        }
        if (disposable == null || disposable.isDisposed()) {
            translateProcessor = ReplayProcessor.create();

            disposable = Observable.concat(
                    memory(),
                    database(localService.getCurrentText(), localService.getCurrentLanguagePair()),
                    network(localService.getCurrentText(), localService.getCurrentLanguagePair())
            )
                    .filter(response -> response.getText() != null).first(new RealmTranslate())
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

    public Single<List<String>> provideLangNames() {
        return Single.just(localService.provideLangNames());
    }

    public void checkFirstTimeUser() {
        localService.checkFirstTimeUser();
    }

    public void swapLanguages() {
        localService.swapLangs();
    }

    public Observable<Boolean> addToFavorites(RealmTranslate realmInstance) {
        return Observable.fromCallable(() -> localService.addToFavorite(realmInstance))
                .onErrorReturn(throwable -> false)
                .compose(schedulerProvider.applyIoSchedulers());
    }

    public Observable<Boolean> removeFromFavorites(RealmTranslate realmInstance) {
        return Observable.fromCallable(() -> localService.deleteFromFavorite(realmInstance))
                .onErrorReturn(throwable -> false)
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<RealmTranslate> network(String text, String language) {
        return networkService.getNetworkCall().getTranslation(API_KEY, text, language)
                .doOnNext(localService::writeToRealm)
                .onErrorReturn(throwable -> {
                    Log.d("RETROFIT", throwable.toString());
                    return new TranslateResponse();
                })
                .map(translateResponse -> memoryCache = localService.responseToRealm(translateResponse))
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<RealmTranslate> database(String text, String languagePair) {
        return Observable.fromCallable(() ->localService.readFromRealm(text, languagePair))
                .compose(schedulerProvider.applyIoSchedulers())
                .doOnNext(realmTranslate -> {
                    if (realmTranslate.getText() != null) {
                        localService.rewriteRealmEntry(realmTranslate);
                        memoryCache = realmTranslate;
                    }
                });
    }

    private Observable<RealmTranslate> memory() {
        return Observable.just(memoryCache);
    }

}
