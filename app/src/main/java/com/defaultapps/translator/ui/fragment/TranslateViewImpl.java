package com.defaultapps.translator.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.activity.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.ui.presenter.TranslateViewPresenterImpl;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.PublishProcessor;


public class TranslateViewImpl extends BaseFragment implements TranslateView {

    private final String TAG = "TranslateViewImpl";
    private boolean editTextStatus = false;

    private MainActivity activity;

    private PublishProcessor<String> publishProcessor;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.errorButton)
    Button errorButton;

    @BindView(R.id.errorTextView)
    TextView errorText;

    @BindView(R.id.translation)
    TextView translatedText;

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
        translateViewPresenter.onAttach(this);
        translateViewPresenter.requestTranslation(false);

        publishProcessor = PublishProcessor.create();
        publishProcessor.onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestedString -> {
                    Log.d(TAG, "EditText changed");
                    translateViewPresenter.setCurrentLanguage("en-ru");
                    translateViewPresenter.setCurrentText(requestedString);
                    if (requestedString.length() != 0) {
                        translateViewPresenter.requestTranslation(true);
                    } else {
                        hideResult();
                    }
                });
        RxTextView.afterTextChangeEvents(editText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (editTextStatus) {
                        publishProcessor.onNext(text.editable().toString());
                    } else {
                        editTextStatus = true;
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        translateViewPresenter.onDetach();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @OnClick(R.id.errorButton)
    void onClick() {
        translateViewPresenter.requestTranslation(true);
    }

    @Override
    public void hideError() {
        errorButton.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        errorButton.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideResult() {
        translatedText.setVisibility(View.GONE);
    }

    @Override
    public void showResult(String result) {
        translatedText.setVisibility(View.VISIBLE);
        translatedText.setText(result);
    }
}
