package com.defaultapps.translator.ui.lang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;


public class LanguageSelectionViewImpl extends BaseFragment implements LanguageSelectionView {

    private static final String ARGUMENT_KEY = "SourceOrTarget";

    private Map<String, String> langListData = new HashMap<>();
    private List<String> values = new ArrayList<>();
    private String fragmentMode;

    private Unbinder unbinder;

    @Inject
    LanguageSelectionPresenterImpl languageSelectionPresenter;

    @Inject
    RxBus rxBus;

    @BindView(R.id.languageList)
    ListView langList;

    public static Fragment newInstance(String sourceOrTarget) {
        Fragment instance = new LanguageSelectionViewImpl();
        Bundle bundle = new Bundle();
        if (sourceOrTarget.equals("source")) {
            bundle.putSerializable(ARGUMENT_KEY, sourceOrTarget);
            instance.setArguments(bundle);
            return instance;
        } else if (sourceOrTarget.equals("target")) {
            bundle.putSerializable(ARGUMENT_KEY, sourceOrTarget);
            instance.setArguments(bundle);
            return instance;
        } else {
            throw new AssertionError("Specify source or target lang.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((LanguageActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        languageSelectionPresenter.onAttach(this);
        fragmentMode = getArguments().getString(ARGUMENT_KEY);
        languageSelectionPresenter.requestLangList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        languageSelectionPresenter.onDetach();
        unbinder.unbind();
    }

    @Override
    public void updateLangList(Map<String, String> langListData) {
        this.langListData = langListData;
        this.values.clear();
        this.values.addAll(langListData.values());
        Collections.sort(values, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.item_lang, values);
        langList.setAdapter(langAdapter);
    }

    @OnItemClick(R.id.languageList)
    void onItemClick(int position) {
        String itemName = values.get(position);
        if (fragmentMode.equals("source")) {
            setSource(itemName, findLangCode(itemName));
            getActivity().finish();
            rxBus.publish(Global.LANG_CHANGED, true);
        } else if (fragmentMode.equals("target")) {
            setTarget(itemName, findLangCode(itemName));
            rxBus.publish(Global.LANG_CHANGED, true);
            getActivity().finish();
        }
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showLoading() {
    }

    private void setSource(String sourceLangName, String sourceLang) {
        languageSelectionPresenter.setSourceLang(sourceLang);
        languageSelectionPresenter.setSourceLangName(sourceLangName);
    }

    private void setTarget(String targetLangName, String targetLang) {
        languageSelectionPresenter.setTargetLang(targetLang);
        languageSelectionPresenter.setTargetLangName(targetLangName);
    }

    private String findLangCode(String langName) {
        String key= null;
        for(Map.Entry<String, String> entry: langListData.entrySet()){
            if(langName.equals(entry.getValue())){
                key = entry.getKey();
                return key;
            }
        }
        return "";
    }
}
