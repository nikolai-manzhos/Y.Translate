package com.defaultapps.translator.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.ui.activity.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.ui.presenter.TranslateViewPresenter;
import com.defaultapps.translator.ui.presenter.TranslateViewPresenterImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class TranslateViewImpl extends BaseFragment implements TranslateView {

    private Unbinder unbinder;
    private MainActivity activity;

    @BindView(R.id.editText)
    EditText editText;

    @Inject
    TranslateViewPresenterImpl translateViewPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity.getActivityComponent().inject(this);
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @OnClick(R.id.button)
    void onClick() {
        translateViewPresenter.requestTranslation(editText.getText().toString());
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void refresh() {

    }
}
