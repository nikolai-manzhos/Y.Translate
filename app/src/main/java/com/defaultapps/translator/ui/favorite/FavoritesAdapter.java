package com.defaultapps.translator.ui.favorite;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {


    public FavoritesAdapter() {

    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        FavoriteViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {

    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
