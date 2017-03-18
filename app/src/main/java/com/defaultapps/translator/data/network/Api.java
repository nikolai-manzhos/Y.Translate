package com.defaultapps.translator.data.network;

import com.defaultapps.translator.data.model.TranslateResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Api {

    @POST("/api/v1.5/tr.json/translate")
    Observable<TranslateResponse> getTranslation(
            @Query("key") String apiKey,
            @Query("text") String text,
            @Query("lang") String language);
}
