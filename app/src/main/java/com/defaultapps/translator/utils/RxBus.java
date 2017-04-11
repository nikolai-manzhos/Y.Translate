package com.defaultapps.translator.utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;


@Singleton
public class RxBus {

    private Map<String, PublishProcessor<Object>> processorMap = new HashMap<>();
    private Map<Object, CompositeDisposable> subscriptionsMap = new HashMap<>();

    @Inject
    public RxBus() {

    }

    public void subscribe(String processor,
                          @NonNull Object lifecycle,
                          @NonNull Consumer<Object> action) {
        Disposable disposable = getProcessor(processor).subscribe(action);
        getCompositeDisposable(lifecycle).add(disposable);
    }

    public void unsubscribe(@NonNull Object lifecycle) {
        CompositeDisposable compositeDisposable = subscriptionsMap.remove(lifecycle);
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public void publish(String processor,
                        @NonNull Object message) {
        getProcessor(processor).onNext(message);
    }

    @NonNull
    private PublishProcessor<Object> getProcessor(String processorKey) {
        PublishProcessor<Object> processor = processorMap.get(processorKey);
        if (processor == null) {
            processor = PublishProcessor.create();
            processor.subscribeOn(AndroidSchedulers.mainThread());
            processorMap.put(processorKey, processor);
        }
        return processor;
    }

    @NonNull
    private CompositeDisposable getCompositeDisposable(@NonNull Object object) {
        CompositeDisposable compositeDisposable = subscriptionsMap.get(object);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            subscriptionsMap.put(object, compositeDisposable);
        }
        return compositeDisposable;
    }


}
