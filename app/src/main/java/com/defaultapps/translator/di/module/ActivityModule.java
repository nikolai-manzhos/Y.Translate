package com.defaultapps.translator.di.module;

import android.app.Activity;
import android.content.Context;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.di.ActivityContext;
import com.defaultapps.translator.ui.presenter.TranslateViewPresenterImpl;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @ActivityContext
    @Provides
    Context provideActivityContext() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

}
