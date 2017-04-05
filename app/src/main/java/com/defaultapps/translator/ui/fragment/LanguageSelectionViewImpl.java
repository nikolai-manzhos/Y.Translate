package com.defaultapps.translator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.activity.LanguageActivity;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.ui.presenter.LanguageSelectionPresenterImpl;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class LanguageSelectionViewImpl extends BaseFragment implements LanguageSelectionView {


    @Inject
    LanguageSelectionPresenterImpl languageSelectionPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((LanguageActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, view);
        languageSelectionPresenter.onAttach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        languageSelectionPresenter.onDetach();
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }
}
