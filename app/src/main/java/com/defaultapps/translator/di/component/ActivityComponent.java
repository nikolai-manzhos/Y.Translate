package com.defaultapps.translator.di.component;

import com.defaultapps.translator.di.module.ActivityModule;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.lang.LanguageActivity;
import com.defaultapps.translator.ui.favorite.FavoritesViewImpl;
import com.defaultapps.translator.ui.history.HistoryViewImpl;
import com.defaultapps.translator.ui.lang.LanguageSelectionViewImpl;
import com.defaultapps.translator.ui.translate.TranslateViewImpl;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TranslateViewImpl translateView);
    void inject(HistoryViewImpl historyView);
    void inject(FavoritesViewImpl favoritesView);
    void inject(LanguageActivity languageActivity);
    void inject(LanguageSelectionViewImpl languageSelectionView);
}
