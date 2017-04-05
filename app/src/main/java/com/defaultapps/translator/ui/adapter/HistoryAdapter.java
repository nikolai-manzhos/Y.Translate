package com.defaultapps.translator.ui.adapter;


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
import com.defaultapps.translator.ui.fragment.HistoryView;
import com.jakewharton.rxbinding2.widget.RxAdapter;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@PerActivity
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private HistoryView view;
    private List<RealmTranslate> data;

    @Inject
    public HistoryAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sourceText)
        TextView sourceText;

        @BindView(R.id.translatedText)
        TextView translatedText;

        @BindView(R.id.languageSet)
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
        setToggleButtonIcon(holder, data.get(adapterPosition).getFavorite());

        RxCompoundButton.checkedChanges(holder.toggleButton)
                .doOnNext(toggleButtonStatus -> {
                    if (view != null)
                        view.favorite(data.get(adapterPosition));
                    setToggleButtonIcon(holder, toggleButtonStatus);
                })
                .subscribe();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_favorite, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<RealmTranslate> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setView(HistoryView view) {
        this.view = view;
    }

    private void setToggleButtonIcon(HistoryViewHolder holder, boolean fav) {
         if (fav) {
             holder.toggleButton.setBackgroundDrawable(new IconDrawable(context, MaterialIcons.md_bookmark).colorRes(R.color.colorAccent));
         }  else {
             holder.toggleButton.setBackgroundDrawable(new IconDrawable(context, MaterialIcons.md_bookmark).colorRes(R.color.grey));
         }
    }
}
