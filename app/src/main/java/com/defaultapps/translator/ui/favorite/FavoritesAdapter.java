package com.defaultapps.translator.ui.favorite;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.scope.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@PerActivity
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private Context context;
    private List<RealmTranslate> data = new ArrayList<>();

    @Inject
    public FavoritesAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sourceText)
        TextView sourceText;

        @BindView(R.id.translatedText)
        TextView translatedText;

        @BindView(R.id.languagePair)
        TextView languagePair;

        @BindView(R.id.favoriteFlag)
        ToggleButton toggleButton;

        FavoriteViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        RealmTranslate realmEntry = data.get(adapterPosition);

        holder.sourceText.setText(realmEntry.getText());
        holder.translatedText.setText(realmEntry.getTranslatedText());
        holder.languagePair.setText(realmEntry.getLanguageSet().toUpperCase());
        holder.toggleButton.setChecked(realmEntry.getFavorite());
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.isEmpty() ? 0 : data.size();
    }

    public void setData(List<RealmTranslate> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
