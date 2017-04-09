package com.defaultapps.translator.ui.favorite;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.main.MainActivity;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesViewImpl extends Fragment implements FavoritesView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.deleteFavorites)
    ImageView deleteFavorites;

    @Inject
    FavoritesViewPresenterImpl favoritesViewPresenter;

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
        initToolbar();
        favoritesViewPresenter.onAttach(this);
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

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    private void initToolbar() {
        deleteFavorites.setImageDrawable(new IconDrawable(
                getActivity().getApplicationContext(),
                MaterialIcons.md_delete
        ).colorRes(R.color.blackPrimary));
    }

    private void initRecyclerView() {

    }

}
