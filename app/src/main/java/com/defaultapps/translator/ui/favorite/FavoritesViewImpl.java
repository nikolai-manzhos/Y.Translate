package com.defaultapps.translator.ui.favorite;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.main.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FavoritesViewImpl extends Fragment implements FavoritesView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.deleteFavorites)
    ImageView deleteFavorites;

    @BindView(R.id.favoriteRecycler)
    RecyclerView favoriteRecycler;

    @BindView(R.id.testButton)
    Button testButton;

    @Inject
    FavoritesViewPresenterImpl favoritesViewPresenter;

    @Inject
    FavoritesAdapter favoritesAdapter;

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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        favoritesViewPresenter.onAttach(this);
        favoritesViewPresenter.requestFavoriteItems();

        initToolbar();
        initRecyclerView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        favoritesViewPresenter.onDetach();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @OnClick(R.id.testButton)
    void onClick() {
        favoritesViewPresenter.requestFavoriteItems();
    }

    @OnClick(R.id.deleteFavorites)
    void onFavoritesDeleteClick() {
        favoritesViewPresenter.deleteFavorites();
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
        DividerItemDecoration divider = new DividerItemDecoration(getActivity().getApplicationContext(), linearLayoutManager.getOrientation());
        favoriteRecycler.setLayoutManager(linearLayoutManager);
        favoriteRecycler.setAdapter(favoritesAdapter);
        favoriteRecycler.addItemDecoration(divider);
    }

}
