package com.defaultapps.translator.ui.history;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.defaultapps.translator.di.ActivityContext;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.scope.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@PerActivity
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> implements Filterable {

    private Context context;
    private HistoryViewPresenterImpl presenter;
    private List<RealmTranslate> data = new ArrayList<>();
    private List<RealmTranslate> originalData = new ArrayList<>();

    @Inject
    public HistoryAdapter(@ActivityContext Context context,
                          HistoryViewPresenterImpl presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemContainer)
        RelativeLayout itemContainer;

        @BindView(R.id.sourceText)
        TextView sourceText;

        @BindView(R.id.translatedText)
        TextView translatedText;

        @BindView(R.id.languagePair)
        TextView languageSet;

        @BindView(R.id.favoriteFlag)
        ToggleButton toggleButton;

        HistoryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();

        holder.sourceText.setText(data.get(adapterPosition).getText());
        holder.translatedText.setText(data.get(adapterPosition).getTranslatedText());
        holder.languageSet.setText(data.get(adapterPosition).getLanguageSet().toUpperCase());
        holder.toggleButton.setChecked(data.get(adapterPosition).getFavorite());

        holder.itemContainer.setOnLongClickListener(containerView -> {
            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                    .setTitle(R.string.history_delete_entry)
                    .setPositiveButton(R.string.alert_ok, (dialog, which) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            Log.d("hAdapter", data.get(adapterPosition).getText());
                            presenter.deleteHistoryItem(data.get(adapterPosition));
                            removeAt(adapterPosition);
                        }
                    })
                    .setNegativeButton(R.string.alert_cancel, (dialog, which) -> {})
                    .show();
            return true;
        });

    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_favorite, parent, false);
        HistoryViewHolder vh = new HistoryViewHolder(view);
        vh.toggleButton.setOnClickListener(toggleView -> {
            boolean status = ((ToggleButton) toggleView).isChecked();
            if (presenter != null && status) {
                presenter.addToFav(data.get(vh.getAdapterPosition()));
            } else if (presenter != null) {
                presenter.deleteFromFav(data.get(vh.getAdapterPosition()));
            }
        });
        vh.itemContainer.setOnClickListener(containerView -> presenter.selectItem(data.get(vh.getAdapterPosition())));

        return vh;
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
        originalData.clear();
        originalData.addAll(data);
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

    protected void removeAt(int position) {
        data.remove(position);
        originalData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }
}
