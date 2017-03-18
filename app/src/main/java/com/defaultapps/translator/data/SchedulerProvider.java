package com.defaultapps.translator.data;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public interface SchedulerProvider {
    <T> ObservableTransformer<T, T> applyIoSchedulers();

    SchedulerProvider DEFAULT = new SchedulerProvider() {
        @Override public <T> ObservableTransformer<T, T> applyIoSchedulers() {
            return observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}
