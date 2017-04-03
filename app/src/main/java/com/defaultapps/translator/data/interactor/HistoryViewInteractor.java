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
public class HistoryViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private ReplayProcessor<List<RealmTranslate>> replayProcessor;
    private Disposable disposable;

    @Inject
    public HistoryViewInteractor(
          SchedulerProvider schedulerProvider,
          LocalService localService
    ) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<List<RealmTranslate>> provideHistoryData() {
        if (disposable == null || disposable.isDisposed()) {
            replayProcessor = ReplayProcessor.create();

            disposable = Observable.just(localService.provideWholeDatabase())
                    .compose(schedulerProvider.applyIoSchedulers())
                    .subscribe(replayProcessor::onNext);
        }
        return replayProcessor.toObservable();
    }
}
