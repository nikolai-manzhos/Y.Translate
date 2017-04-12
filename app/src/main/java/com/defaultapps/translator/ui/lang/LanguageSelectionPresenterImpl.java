package com.defaultapps.translator.ui.lang;

import android.util.Log;

import com.defaultapps.translator.data.interactor.LanguageViewInteractor;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class LanguageSelectionPresenterImpl extends BasePresenter<LanguageSelectionView> implements LanguageSelectionPresenter {

    private LanguageViewInteractor languageViewInteractor;

    @Inject
    public LanguageSelectionPresenterImpl(CompositeDisposable compositeDisposable,
                                          LanguageViewInteractor languageViewInteractor) {
        super(compositeDisposable);
        this.languageViewInteractor = languageViewInteractor;
    }

    @Override
    public void requestLangList() {
        getCompositeDisposable().add(
                languageViewInteractor.requestSourceLang()
                .subscribe(result -> {
                    if (getView() != null) {
                        getView().updateLangList(result);
                        Log.d("SourceList", result.toString());
                    }
                })
        );
    }

    @Override
    public void setSourceLang(String sourceLang) {
        languageViewInteractor.setSourceLang(sourceLang);
    }

    @Override
    public void setSourceLangName(String sourceLangName) {
        languageViewInteractor.setSourceLangName(sourceLangName);
    }

    @Override
    public void setTargetLang(String targetLang) {
        languageViewInteractor.setTargetLang(targetLang);
    }

    @Override
    public void setTargetLangName(String targetLangName) {
        languageViewInteractor.setTargetLangName(targetLangName);
    }
}
