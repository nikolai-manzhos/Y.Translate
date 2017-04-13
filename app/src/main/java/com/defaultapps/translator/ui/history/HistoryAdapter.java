package com.defaultapps.translator.ui.history;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.scope.PerActivity;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.disposables.Disposable;

@PerActivity
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private HistoryViewPresenterImpl presenter;
    private List<RealmTranslate> data = new ArrayList<>();
    private Disposable favSubscr;

    @Inject
    public HistoryAdapter(@ApplicationContext Context context,
                          HistoryViewPresenterImpl presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

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
        return vh;
    }

    @Override
    public int getItemCount() {
        return data.isEmpty() ? 0 : data.size();
    }

    public void setData(List<RealmTranslate> data) {
        if (favSubscr != null) {
            favSubscr.dispose();
        }
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
