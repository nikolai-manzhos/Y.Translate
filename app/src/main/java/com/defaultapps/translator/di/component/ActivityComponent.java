package com.defaultapps.translator.di.component;

import com.defaultapps.translator.di.module.ActivityModule;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.fragment.FavoritesViewImpl;
import com.defaultapps.translator.ui.fragment.HistoryViewImpl;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TranslateViewImpl translateView);
    void inject(HistoryViewImpl historyView);
    void inject(FavoritesViewImpl favoritesView);
}
