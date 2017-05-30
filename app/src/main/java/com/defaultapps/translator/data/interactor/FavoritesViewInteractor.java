package com.defaultapps.translator.data.interactor;


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
public class FavoritesViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private Disposable disposable;
    private Disposable wipeDisposable;
    private Disposable selectItemDisposable;
    private Disposable deleteItemDisposable;
    private ReplaySubject<Boolean> wipeReplayProcessor;
    private ReplaySubject<Boolean> selectItemReplayProcessor;
    private ReplaySubject<Boolean> deleteItemReplayProcessor;
    private ReplaySubject<List<RealmTranslate>> replayProcessor;

    @Inject
    public FavoritesViewInteractor(SchedulerProvider schedulerProvider,
                                   LocalService localService) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<List<RealmTranslate>> provideFavoritesData() {
        if (disposable == null || disposable.isDisposed()) {
            replayProcessor = ReplaySubject.create();

            disposable = Observable.fromCallable(localService::provideFavoritesDatabase)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(replayProcessor::onNext);
        }
        return replayProcessor;
    }

    public Observable<Boolean> deleteFavoritesData() {
        if (wipeDisposable == null || wipeDisposable.isDisposed()) {
            wipeReplayProcessor = ReplaySubject.create();

            wipeDisposable = Observable.fromCallable(localService::wipeFavorites)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(wipeReplayProcessor::onNext);
        }
        return wipeReplayProcessor;
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

    public Observable<Boolean> deleteFavoriteItem(RealmTranslate realmInstance) {
        if (deleteItemDisposable == null || deleteItemDisposable.isDisposed()) {
            deleteItemReplayProcessor = ReplaySubject.create();

            deleteItemDisposable = Observable.fromCallable(() -> localService.removeItemFromFavorites(realmInstance))
                    .compose(schedulerProvider.applyIoSchedulers())
                    .onErrorReturn(throwable -> false)
                    .subscribe(deleteItemReplayProcessor::onNext);
        }
        return deleteItemReplayProcessor;
    }
}
