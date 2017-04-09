package com.defaultapps.translator.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmTranslate extends RealmObject {
    @PrimaryKey
    private String text;
    private String translatedText;
    private boolean favorite;
    private boolean history;
    private String languageSet;

    public RealmTranslate() {}

    public RealmTranslate(String text,
                          String translatedText,
                          boolean favorite,
                          boolean history,
                          String languageSet) {
        this.text = text;
        this.translatedText = translatedText;
        this.favorite = favorite;
        this.history = history;
        this.languageSet = languageSet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public String getLanguageSet() {
        return languageSet;
    }

    public void setLanguageSet(String languageSet) {
        this.languageSet = languageSet;
    }
}
