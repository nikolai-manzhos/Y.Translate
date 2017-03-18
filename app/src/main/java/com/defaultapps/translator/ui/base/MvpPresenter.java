package com.defaultapps.translator.ui.base;


public interface MvpPresenter<T extends MvpView> {
    void onAttach(T view);
    void onDetach();
}
