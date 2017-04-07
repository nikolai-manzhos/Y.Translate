package com.defaultapps.translator.ui.history;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.di.ApplicationContext;
import com.defaultapps.translator.di.scope.PerActivity;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import scout.core.Scanner$reify__163;

@PerActivity
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private HistoryView view;
    private List<RealmTranslate> data = new ArrayList<>();

    private IconDrawable greyIcon;
    private IconDrawable coloredIcon;

    @Inject
    public HistoryAdapter(@ApplicationContext Context context) {
        this.context = context;
        greyIcon = new IconDrawable(this.context, MaterialIcons.md_bookmark).colorRes(R.color.grey);
        coloredIcon = new IconDrawable(this.context, MaterialIcons.md_bookmark).colorRes(R.color.colorAccent);
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
        RxCompoundButton.checkedChanges(holder.toggleButton)
                .skip(1)
                .subscribe(status -> {
                    Log.d("Adapter", String.valueOf(status));
                    setToggleButtonIcon(holder, status);
                    if (view != null && status) {
                        view.favorite(data.get(adapterPosition));
                    } else if (view != null) {
                        view.delFromFavorite(data.get(adapterPosition));
                    }
                });
        holder.sourceText.setText(data.get(adapterPosition).getText());
        holder.translatedText.setText(data.get(adapterPosition).getTranslatedText());
        holder.languageSet.setText(data.get(adapterPosition).getLanguageSet().toUpperCase());
        setToggleButtonIcon(holder, data.get(adapterPosition).getFavorite());
        holder.toggleButton.setChecked(data.get(adapterPosition).getFavorite());


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
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setView(HistoryView view) {
        this.view = view;
    }

    private void setToggleButtonIcon(HistoryViewHolder holder, boolean fav) {
         if (fav) {
             holder.toggleButton.setBackgroundDrawable(coloredIcon);
         }  else {
             holder.toggleButton.setBackgroundDrawable(greyIcon);
         }
    }
}
