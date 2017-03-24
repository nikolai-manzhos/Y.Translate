package com.defaultapps.translator.data.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class RealmTranslate extends RealmObject {
    @PrimaryKey
    private String text;
    private String translatedText;
    private boolean favorite;
    private String languageSet;

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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getLanguageSet() {
        return languageSet;
    }

    public void setLanguageSet(String languageSet) {
        this.languageSet = languageSet;
    }
}
