package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.translate.TranslateView;
import com.defaultapps.translator.ui.translate.TranslateViewPresenterImpl;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.tests.utils.TestSuiteChunker;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.TestScheduler;

import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class TranslateViewPresenterTest {
    @Mock
    private TranslateViewInteractor translateViewInteractor;
    @Mock
    private TranslateView translateView;
    @Mock
    private RxBus rxBus;

    private TranslateViewPresenterImpl translateViewPresenter;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String SOURCE_WORD = "hello";
    private static final String TRANSLATED_WORD = "привет";
    private static final String LANGUAGE_PAIR = "en-ru";
    private static final String SOURCE_LNG_NAME = "English";
    private static final String TARGET_LNG_NAME = "Russian";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        translateViewPresenter = new TranslateViewPresenterImpl(translateViewInteractor, compositeDisposable, rxBus);

    }

    @Test
    public void testSuccesScenario() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        RealmTranslate entity = provideRealmEntry();
        Observable<RealmTranslate> resultObserv = just(entity).subscribeOn(testScheduler);
        when(translateViewInteractor.requestTranslation(anyBoolean())).thenReturn(resultObserv);

        translateViewPresenter.onAttach(translateView);
        translateViewPresenter.requestTranslation(true);

        verify(translateView).hideResult();
        verify(translateView).hideError();
        verify(translateView).showLoading();

        testScheduler.triggerActions();

        verify(translateView).hideLoading();
        verify(translateView).showResult();
        verify(translateView).deliverData(entity);
        verify(rxBus).publish(Global.HISTORY_UPDATE, true);
    }

    @Test
    public void testErrorScenario() throws Exception {
        when(translateViewInteractor.requestTranslation(true)).thenReturn(Observable.error(new Exception("some error")));

        translateViewPresenter.onAttach(translateView);
        translateViewPresenter.requestTranslation(true);

        verify(translateView, times(2)).hideResult();
        verify(translateView).hideError();
        verify(translateView).showLoading();

        verify(translateView).hideLoading();
        verify(translateView).showError();
        verify(translateView, never()).deliverData(anyObject());
    }

    @Test(expected = NullPointerException.class)
    public void noViewAttachedTest() throws Exception {
        translateViewPresenter.onDetach();

        translateViewPresenter.requestTranslation(true);

        verify(translateView,never()).showLoading();
        verify(translateView, never()).showError();
    }

    @Test
    public void requestLangNamesTest() throws Exception {
        List<String> langList = new ArrayList<>();
        langList.add(SOURCE_LNG_NAME);
        langList.add(TARGET_LNG_NAME);
        TestScheduler testScheduler = new TestScheduler();
        Single<List<String>> result = Single.just(langList).subscribeOn(testScheduler);
        when(translateViewInteractor.provideLangNames()).thenReturn(result);

        translateViewPresenter.onAttach(translateView);
        translateViewPresenter.requestLangNames();

        testScheduler.triggerActions();

        verify(translateView).setLangNames(langList.get(0), langList.get(1));
    }

    @Test
    public void addToFavoritesTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(translateViewInteractor.addToFavorites(entity)).thenReturn(result);

        translateViewPresenter.onAttach(translateView);
        translateViewPresenter.addToFavorites(entity);

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.HISTORY_UPDATE, true);
        verify(rxBus).publish(Global.FAVORITES_UPDATE, true);
    }

    @Test
    public void deleteFromFavoritesTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(translateViewInteractor.removeFromFavorites(entity)).thenReturn(result);

        translateViewPresenter.onAttach(translateView);
        translateViewPresenter.deleteFromFavorites(entity);

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.HISTORY_UPDATE, true);
        verify(rxBus).publish(Global.FAVORITES_UPDATE, true);
    }

    private RealmTranslate provideRealmEntry() {
        return new RealmTranslate(SOURCE_WORD + LANGUAGE_PAIR,
                SOURCE_WORD,
                TRANSLATED_WORD,
                false,
                true,
                LANGUAGE_PAIR,
                SOURCE_LNG_NAME,
                TARGET_LNG_NAME);
    }
}