package com.defaultapps.translator.ui.base;

import com.defaultapps.translator.di.scope.PerActivity;

import io.reactivex.disposables.CompositeDisposable;

@PerActivity
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V view;
    private CompositeDisposable compositeDisposable;

    public BasePresenter(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        compositeDisposable.clear();
        this.view = null;
    }

    protected V getView() {
        return this.view;
    }

    protected CompositeDisposable getCompositeDisposable() {
        return this.compositeDisposable;
    }
}
