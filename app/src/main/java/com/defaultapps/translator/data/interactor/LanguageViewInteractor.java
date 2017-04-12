package com.defaultapps.translator.data.interactor;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.processors.ReplayProcessor;


public class LanguageViewInteractor {

    private final SchedulerProvider schedulerProvider;
    private final LocalService localService;

    private Map<String, String> memoryCache = new HashMap<>();
    private ReplayProcessor<Map<String, String>> replayProcessor;

    @Inject
    public LanguageViewInteractor(SchedulerProvider schedulerProvider,
                                  LocalService localService) {
        this.schedulerProvider = schedulerProvider;
        this.localService = localService;
    }

    public Observable<Map<String, String>> requestSourceLang() {
        replayProcessor = ReplayProcessor.create();
        Observable.concat(
                memory(),
                local())
                .filter(response -> !response.isEmpty()).first(new HashMap<>())
                .subscribe(replayProcessor::onNext);
        return replayProcessor.toObservable();
    }

    public void setSourceLang(String sourceLang) {
        localService.setSourceLang(sourceLang);
    }

    public void setSourceLangName(String sourceLangName) {
        localService.setSourceLangName(sourceLangName);
    }

    public void setTargetLang(String targetLang) {
        localService.setTargetLang(targetLang);
    }

    public void setTargetLangName(String targetLangName) {
        localService.setTargetLangName(targetLangName);
    }

    private Observable<Map<String, String>> local() {
        return Observable.fromCallable(() -> localService.readLangFromFile())
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<Map<String, String>> memory() {
        return Observable.just(memoryCache);
    }
}
