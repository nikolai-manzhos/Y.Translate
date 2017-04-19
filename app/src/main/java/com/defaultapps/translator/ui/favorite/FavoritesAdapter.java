package com.defaultapps.translator.ui.favorite;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
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
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> implements Filterable{

    private Context context;
    private List<RealmTranslate> data = new ArrayList<>();
    private List<RealmTranslate> originalData = new ArrayList<>();
    private FavoritesViewPresenterImpl presenter;

    @Inject
    public FavoritesAdapter(@ApplicationContext Context context,
                            FavoritesViewPresenterImpl presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemContainer)
        RelativeLayout itemContainer;

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
        holder.itemContainer.setOnClickListener(view -> presenter.selectItem(realmEntry));
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<RealmTranslate> filteredResults;
                if (charSequence.length() == 0) {
                    filteredResults = originalData;
                } else {
                    filteredResults = getFilteredResults(charSequence.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data.clear();
                data.addAll((List<RealmTranslate>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setData(List<RealmTranslate> data) {
        this.data.clear();
        this.data.addAll(data);
        this.originalData.clear();
        this.originalData.addAll(data);
        notifyDataSetChanged();
    }

    protected List<RealmTranslate> getFilteredResults(String constraint) {
        List<RealmTranslate> results = new ArrayList<>();

        for (RealmTranslate item : data) {
            if (item.getText().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
