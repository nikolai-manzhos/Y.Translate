package com.defaultapps.translator.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.defaultapps.translator.di.ActivityContext;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.activity.MainActivity;

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

    @PerActivity
    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @PerActivity
    @Provides
    FragmentManager provideFragmentManager() {
        return ((MainActivity) activity).getSupportFragmentManager();
    }

}
