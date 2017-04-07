package com.defaultapps.translator.ui.history;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.ui.main.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HistoryViewImpl extends Fragment implements HistoryView {

    @BindView(R.id.historyRecycler)
    RecyclerView historyRecycler;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    HistoryViewPresenterImpl historyViewPresenter;

    @PerActivity
    @Inject
    HistoryAdapter historyAdapter;

    private MainActivity activity;
    private Unbinder unbinder;


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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        toolbar.setTitle("History");
        historyAdapter.setView(this);
        initRecyclerView();
        historyViewPresenter.onAttach(this);
        historyViewPresenter.requestHistoryItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("History", "OnResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("HistoryView", "DESTROYED");
        unbinder.unbind();
        historyAdapter.setView(null);
        historyViewPresenter.onDetach();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @OnClick(R.id.testButton)
    void onClick() {
        Log.d("HistoryView", "onClick");
        historyViewPresenter.requestHistoryItems();
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void favorite(RealmTranslate realmObject) {
        historyViewPresenter.addToFav(realmObject);
    }

    @Override
    public void delFromFavorite(RealmTranslate realmTranslate) {
        historyViewPresenter.deleteFromFav(realmTranslate);
    }

    @Override
    public void receiveResult(List<RealmTranslate> realmTranslateList) {
        if (!realmTranslateList.isEmpty()) {
            historyAdapter.setData(realmTranslateList);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity().getApplicationContext(), linearLayoutManager.getOrientation());
        historyRecycler.setLayoutManager(linearLayoutManager);
        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.addItemDecoration(divider);
    }
}
