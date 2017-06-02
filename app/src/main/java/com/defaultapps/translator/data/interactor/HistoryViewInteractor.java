package com.defaultapps.translator.data.interactor;

import android.util.Log;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.realm.RealmTranslate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;

@Singleton
public class HistoryViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private ReplaySubject<List<RealmTranslate>> replayProcessor;
    private ReplaySubject<Boolean> favReplayProcessor;
    private ReplaySubject<Boolean> wipeReplayProcessor;
    private ReplaySubject<Boolean> selectItemReplayProcessor;
    private Disposable disposable;
    private Disposable favDisposable;
    private Disposable wipeDisposable;
    private Disposable selectItemDisposable;

    @Inject
    HistoryViewInteractor(
          SchedulerProvider schedulerProvider,
          LocalService localService) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<List<RealmTranslate>> provideHistoryData() {
        if (disposable == null || disposable.isDisposed()) {
            replayProcessor = ReplaySubject.create();

            disposable = Observable.fromCallable(localService::provideHistoryDatabase)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(replayProcessor::onNext, replayProcessor::onError);
        }
        return replayProcessor;
    }

    public Observable<Boolean> deleteHistoryData() {
        if (wipeDisposable == null || wipeDisposable.isDisposed()) {
            wipeReplayProcessor = ReplaySubject.create();

            wipeDisposable = Observable.fromCallable(localService::wipeHistory)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .onErrorReturn(throwable -> {
                        Log.d("HistoryInteractor", "wipe history data error: " + throwable.toString());
                        return false;
                    })
                    .subscribe(wipeReplayProcessor::onNext);
        }
        return wipeReplayProcessor;
    }

    public Observable<Boolean> addToFavorite(RealmTranslate realmTranslate) {
        if (favDisposable == null || disposable.isDisposed()) {
            favReplayProcessor = ReplaySubject.create();

            favDisposable = Observable.fromCallable(() -> localService.addToFavorite(realmTranslate))
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(favReplayProcessor::onNext);
        }
        return favReplayProcessor;
    }

    public Observable<Boolean> deleteFromFavorite(RealmTranslate realmTranslate) {
        if (favDisposable == null || disposable.isDisposed()) {
            favReplayProcessor = ReplaySubject.create();

            favDisposable = Observable.fromCallable(() -> localService.deleteFromFavorite(realmTranslate))
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(favReplayProcessor::onNext);
        }
        return favReplayProcessor;
    }

    public Observable<Boolean> setCurrentParams(RealmTranslate realmInstance) {
        if (selectItemDisposable == null || selectItemDisposable.isDisposed()) {
            selectItemReplayProcessor = ReplaySubject.create();

            selectItemDisposable = Observable.just(localService.setCurrentParams(realmInstance))
                    .onErrorReturnItem(false)
                    .subscribe(selectItemReplayProcessor::onNext);
        }
        return selectItemReplayProcessor;
    }

    public void removeHistoryItem(RealmTranslate realmInstance) {
        localService.removeItemFromHistory(realmInstance);
    }
}
