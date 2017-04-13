package com.defaultapps.translator.di.module;

import android.app.Application;
import android.content.Context;

import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.local.sp.SharedPreferencesHelper;
import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.DEFAULT;
    }

    @Provides
    LocalService provideLocalService(SharedPreferencesManager sharedPreferencesManager) {
        return new LocalService(sharedPreferencesManager, application);
    }


}
