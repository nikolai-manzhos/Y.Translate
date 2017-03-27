package com.defaultapps.translator;

import android.app.Application;

import com.defaultapps.translator.di.component.ApplicationComponent;
import com.defaultapps.translator.di.component.DaggerApplicationComponent;
import com.defaultapps.translator.di.module.ApplicationModule;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;

import io.realm.Realm;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initIconify();
        Realm.init(this);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    private void initIconify() {
        Iconify
                .with(new MaterialModule());
    }
}
