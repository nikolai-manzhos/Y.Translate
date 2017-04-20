package com.defaultapps.translator.ui.favorite;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.base.BaseFragment;
import com.defaultapps.translator.ui.main.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FavoritesViewImpl extends BaseFragment implements FavoritesView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.deleteFavorites)
    ImageView deleteFavorites;

    @BindView(R.id.favoriteRecycler)
    RecyclerView favoriteRecycler;

    @BindView(R.id.favNoData)
    LinearLayout noDataView;

    @BindView(R.id.searchFavorites)
    EditText searchFavorites;

    @Inject
    FavoritesViewPresenterImpl favoritesViewPresenter;

    @Inject
    FavoritesAdapter favoritesAdapter;

    @Inject
    RxBus rxBus;

    private Unbinder unbinder;
    private Resources resources;
    private Disposable searchDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resources = getActivity().getApplicationContext().getResources();
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        favoritesViewPresenter.onAttach(this);
        favoritesViewPresenter.requestFavoriteItems();

        initToolbar();
        initRecyclerView();

        rxBus.subscribe(Global.FAVORITES_UPDATE,
                this,
                message -> {
                    if ((boolean) message) favoritesViewPresenter.requestFavoriteItems();
                });

        searchDisposable = RxTextView.textChangeEvents(searchFavorites)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        textViewTextChangeEvent -> favoritesAdapter.getFilter().filter(textViewTextChangeEvent.text())
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        favoritesViewPresenter.onDetach();
        rxBus.unsubscribe(this);
        searchDisposable.dispose();
    }

    @OnClick(R.id.deleteFavorites)
    void onFavoritesDeleteClick() {
        displayDialog(resources.getString(R.string.favorites),
                resources.getString(R.string.favorites_delete_all),
                (dialog, which) ->  {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        favoritesViewPresenter.deleteFavorites();
                    }
                });
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void receiveResult(List<RealmTranslate> realmTranslateList) {
        favoritesAdapter.setData(realmTranslateList);
        favoriteRecycler.scrollToPosition(realmTranslateList.size() - 1);
    }

    @Override
    public void showNoDataView() {
        noDataView.setVisibility(View.VISIBLE);
        deleteFavorites.setVisibility(View.GONE);
        searchFavorites.setVisibility(View.GONE);
    }

    @Override
    public void hideNoDataView() {
        noDataView.setVisibility(View.GONE);
        deleteFavorites.setVisibility(View.VISIBLE);
        searchFavorites.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        deleteFavorites.setImageDrawable(new IconDrawable(
                getActivity().getApplicationContext(),
                MaterialIcons.md_delete
        ).colorRes(R.color.blackPrimary));
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        favoriteRecycler.setLayoutManager(linearLayoutManager);
        favoriteRecycler.setAdapter(favoritesAdapter);
    }
}
