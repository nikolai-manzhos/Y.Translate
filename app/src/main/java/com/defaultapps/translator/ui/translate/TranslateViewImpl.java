package com.defaultapps.translator.ui.translate;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.ui.lang.LanguageActivity;
import com.defaultapps.translator.ui.main.MainActivity;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class TranslateViewImpl extends BaseFragment implements TranslateView {


    private Unbinder unbinder;
    private Disposable disposable;

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

    @BindView(R.id.swapLanguages)
    ImageButton swipeLanguagesButton;

    @BindView(R.id.sourceLanguageName)
    TextView sourceLanguageName;

    @BindView(R.id.targetLanguageName)
    TextView targetLanguageName;

    @BindView(R.id.favoriteFlag)
    ToggleButton favoriteToggle;

    @Inject
    TranslateViewPresenterImpl translateViewPresenter;

    @Inject
    RxBus rxBus;

    @Inject
    @ApplicationContext
    Context applicationContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        initSwipeButton();
        translateViewPresenter.onAttach(this);
        translateViewPresenter.checkFirstTimeUser();
        translateViewPresenter.requestLangNames();
        if (!translateViewPresenter.getCurrentText().equals("")) {
            editText.setText(translateViewPresenter.getCurrentText());
            translateViewPresenter.requestTranslation(false);
        }

        disposable = RxTextView.textChangeEvents(editText)
                .debounce(700, TimeUnit.MILLISECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {
            translateViewPresenter.setCurrentText(text.text().toString());
            if (text.text().length() != 0 && !text.text().toString().trim().isEmpty()) {
                translateViewPresenter.requestTranslation(true);
            } else if (getView() != null){
                hideResult();
                hideError();
            }
        });

        rxBus.subscribe(Global.LANG_CHANGED,
                this,
                message -> {
                    if ((boolean) message) {
                        translateViewPresenter.requestLangNames();
                        if (!editText.getText().toString().trim().isEmpty()) {
                            translateViewPresenter.requestTranslation(true);
                        }
                    }
                });

        rxBus.subscribe(Global.TRANSLATE_UPDATE,
                this,
                message -> {
                    if ((boolean) message) {
                        translateViewPresenter.requestLangNames();
                        editText.setText(translateViewPresenter.getCurrentText());
                    }
                });

        rxBus.subscribe(Global.FAVORITE_CHANGED,
                this,
                message -> {
                    if (message instanceof Boolean) {
                        favoriteToggle.setChecked(false);
                        return;
                    }
                    RealmTranslate instance = (RealmTranslate) message;
                    if (instance.getText().equals(editText.getText().toString())
                            && instance.getTargetLangName().equals(targetLanguageName.getText())
                            && instance.getSourceLangName().equals(sourceLanguageName.getText())) {
                        favoriteToggle.setChecked(instance.getFavorite());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        translateViewPresenter.onDetach();
        rxBus.unsubscribe(this);
        disposable.dispose();
    }

    @OnClick(R.id.errorButton)
    void onClick() {
        translateViewPresenter.requestTranslation(true);
    }

    @OnClick(R.id.sourceLanguageName)
    void onSourceClick() {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        intent.putExtra(Global.SOURCE_OR_TARGET, "source");
        startActivity(intent);
    }

    @OnClick(R.id.targetLanguageName)
    void onTargetClick() {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        intent.putExtra(Global.SOURCE_OR_TARGET, "target");
        startActivity(intent);
    }

    @OnClick(R.id.translation)
    void onTranslatedTextClick(TextView view) {
        Toast.makeText(applicationContext, "Copied to clipboard.", Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Translator", view.getText());
        clipboard.setPrimaryClip(clip);
    }

    @OnClick(R.id.swapLanguages)
    void onSwapClick() {
        translateViewPresenter.swapLanguages();
        translateViewPresenter.requestLangNames();
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
        favoriteToggle.setVisibility(View.GONE);
    }

    @Override
    public void showResult() {
        translatedText.setVisibility(View.VISIBLE);
        favoriteToggle.setVisibility(View.VISIBLE);
    }

    @Override
    public void deliverData(RealmTranslate realmInstance) {

        favoriteToggle.setOnClickListener(view -> {
            Log.d("TranslateView", "Triggered");
            boolean status = ((ToggleButton) view).isChecked();
            if (status) {
                translateViewPresenter.addToFavorites(realmInstance);
            } else {
                translateViewPresenter.deleteFromFavorites(realmInstance);
            }
        });

        translatedText.setText(realmInstance.getTranslatedText());
        favoriteToggle.setChecked(realmInstance.getFavorite());
    }

    @Override
    public void setLangNames(String sourceLangName, String targetLangName) {
        this.sourceLanguageName.setText(sourceLangName);
        this.targetLanguageName.setText(targetLangName);
    }

    private void initSwipeButton() {
        swipeLanguagesButton.setImageDrawable(new IconDrawable(
                getContext().getApplicationContext(),
                MaterialIcons.md_swap_horiz)
                .colorRes(R.color.blackPrimary)
        );
    }
}
