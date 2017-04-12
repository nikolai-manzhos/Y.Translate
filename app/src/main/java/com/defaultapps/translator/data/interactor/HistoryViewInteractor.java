package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.realm.RealmTranslate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import clojure.lang.IFn;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.ReplayProcessor;

@Singleton
public class HistoryViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private ReplayProcessor<List<RealmTranslate>> replayProcessor;
    private ReplayProcessor<Boolean> favReplayProcessor;
    private ReplayProcessor<Boolean> wipeReplayProcessor;
    private Disposable disposable;
    private Disposable favDisposable;
    private Disposable wipeDisposable;

    @Inject
    public HistoryViewInteractor(
          SchedulerProvider schedulerProvider,
          LocalService localService) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<List<RealmTranslate>> provideHistoryData() {
        if (disposable == null || disposable.isDisposed()) {
            replayProcessor = ReplayProcessor.create();

            disposable = Observable.fromCallable(localService::provideHistoryDatabase)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(replayProcessor::onNext);
        }
        return replayProcessor.toObservable();
    }

    public Observable<Boolean> deleteHistoryData() {
        if (wipeDisposable == null || wipeDisposable.isDisposed()) {
            wipeReplayProcessor = ReplayProcessor.create();

            wipeDisposable = Observable.fromCallable(localService::wipeHistory)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .onErrorReturn(throwable -> {
                        Log.d("HistoryInteractor", "wipe history data error: " + throwable.toString());
                        return false;
                    })
                    .subscribe(wipeReplayProcessor::onNext);
        }
        return wipeReplayProcessor.toObservable();
    }

    public Observable<Boolean> addToFavorite(RealmTranslate realmTranslate) {
        if (favDisposable == null || disposable.isDisposed()) {
            favReplayProcessor = ReplayProcessor.create();

            favDisposable = Observable.fromCallable(() -> localService.addToFavorite(realmTranslate))
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(favReplayProcessor::onNext);
        }
        return favReplayProcessor.toObservable();
    }

    public Observable<Boolean> deleteFromFavorite(RealmTranslate realmTranslate) {
        if (favDisposable == null || disposable.isDisposed()) {
            favReplayProcessor = ReplayProcessor.create();

            favDisposable = Observable.fromCallable(() -> localService.deleteFromFavorite(realmTranslate))
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(favReplayProcessor::onNext);
        }
        return favReplayProcessor.toObservable();
    }
}
