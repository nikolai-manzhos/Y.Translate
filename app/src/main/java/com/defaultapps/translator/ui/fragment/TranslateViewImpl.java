package com.defaultapps.translator.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.activity.LanguageActivity;
import com.defaultapps.translator.ui.activity.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.ui.presenter.TranslateViewPresenterImpl;
import com.defaultapps.translator.utils.Global;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;


public class TranslateViewImpl extends BaseFragment implements TranslateView {

    private final String TAG = "TranslateViewImpl";
    private boolean editTextStatus = false;

    private MainActivity activity;
    private Observable<TextViewTextChangeEvent> textChangeObservable;

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

    @BindView(R.id.swipeLanguages)
    ImageButton swipeLanguagesButton;

    @Inject
    TranslateViewPresenterImpl translateViewPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            activity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwipeButton();
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        translateViewPresenter.onAttach(this);
        if (!translateViewPresenter.getCurrentText().equals("")) {
            editText.setText(translateViewPresenter.getCurrentText());
            translateViewPresenter.requestTranslation(false);
            editTextStatus = false;
        }

        textChangeObservable = RxTextView.textChangeEvents(editText)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        textChangeObservable.subscribe(text -> {
            if (editTextStatus) {
                translateViewPresenter.setCurrentText(text.text().toString());
                if (text.text().length() != 0) {
                    translateViewPresenter.requestTranslation(true);
                } else if (getView() != null){
                    hideResult();
                    hideError();
                }
            } else {
                editTextStatus = true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        translateViewPresenter.onDetach();
        textChangeObservable.unsubscribeOn(AndroidSchedulers.mainThread());
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

    @OnClick(R.id.sourceLanguage)
    void onSourceClick() {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        intent.putExtra(Global.SOURCE_OR_TARGET, "source");
        startActivity(intent);
    }

    @OnClick(R.id.targetLanguage)
    void onTargetClick() {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        intent.putExtra(Global.SOURCE_OR_TARGET, "target");
        startActivity(intent);
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

    private void initSwipeButton() {
        swipeLanguagesButton.setImageDrawable(new IconDrawable(
                getContext().getApplicationContext(),
                MaterialIcons.md_swap_horiz)
                .colorRes(R.color.whiteSecondary)
        );
    }
}
