package com.defaultapps.translator.di.component;


import android.content.Context;

import com.defaultapps.translator.App;
import com.defaultapps.translator.data.SchedulerProvider;
import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.data.local.LocalService;
import com.defaultapps.translator.data.network.NetworkService;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    @ApplicationContext
    Context context();

    SchedulerProvider schedulerProvider();

    LocalService localService();

    NetworkService networkService();

    TranslateViewInteractor translateViewInteractor();
}
