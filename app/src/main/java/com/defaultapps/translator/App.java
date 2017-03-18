package com.defaultapps.translator;

import android.app.Application;

import com.defaultapps.translator.di.component.ApplicationComponent;
import com.defaultapps.translator.di.component.DaggerApplicationComponent;
import com.defaultapps.translator.di.module.ApplicationModule;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
