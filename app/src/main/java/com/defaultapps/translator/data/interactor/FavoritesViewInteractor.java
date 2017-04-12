package com.defaultapps.translator.data.interactor;


import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.model.realm.RealmTranslate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.ReplayProcessor;

@Singleton
public class FavoritesViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private Disposable disposable;
    private Disposable wipeDisposable;
    private ReplayProcessor<Boolean> wipeReplayProcessor;
    private ReplayProcessor<List<RealmTranslate>> replayProcessor;

    @Inject
    public FavoritesViewInteractor(SchedulerProvider schedulerProvider,
                                   LocalService localService) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<List<RealmTranslate>> provideFavoritesData() {
        if (disposable == null || disposable.isDisposed()) {
            replayProcessor = ReplayProcessor.create();

            disposable = Observable.fromCallable(localService::provideFavoritesDatabase)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(replayProcessor::onNext);
        }
        return replayProcessor.toObservable();
    }

    public Observable<Boolean> deleteFavoritesData() {
        if (wipeDisposable == null || wipeDisposable.isDisposed()) {
            wipeReplayProcessor = ReplayProcessor.create();

            wipeDisposable = Observable.fromCallable(localService::wipeFavorites)
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(wipeReplayProcessor::onNext);
        }
        return wipeReplayProcessor.toObservable();
    }
}
