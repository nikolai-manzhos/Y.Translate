package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.data.interactor.TranslateViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.fragment.TranslateView;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.TestScheduler;

import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TranslateViewPresenterTest {

    private TranslateViewPresenterImpl translateViewPresenter;

    private RealmTranslate mockRealmResponse;

    @Mock
    private TranslateView translateView;

    @Mock
    private TranslateViewInteractor translateViewInteractor;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final String TEXT = "example";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        translateViewPresenter = new TranslateViewPresenterImpl(translateViewInteractor, compositeDisposable);
        translateViewPresenter.onAttach(translateView);
        mockRealmResponse = new RealmTranslate();
        mockRealmResponse.setTranslatedText(TEXT);
    }

    @Test
    public void testSuccessScenario() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        Observable<RealmTranslate> result = just(mockRealmResponse).subscribeOn(testScheduler);
        when(translateViewInteractor.requestTranslation(true)).thenReturn(result);
        translateViewPresenter.requestTranslation(true);

        testScheduler.triggerActions();

        verify(translateView).hideResult();
        verify(translateView, times(2)).hideError(); // First called when requestTranslation(true); from presenter is called, second time after result arrives
        verify(translateView).showLoading();

        verify(translateView, times(2)).hideLoading(); // onNext, onComplete
        verify(translateView).showResult(TEXT);
    }

    @Test
    public void testErrorScenario() throws Exception {
        mockRealmResponse.setTranslatedText(null);
        TestScheduler testScheduler = new TestScheduler();
        Observable<RealmTranslate> result = just(mockRealmResponse).subscribeOn(testScheduler);
        when(translateViewInteractor.requestTranslation(true)).thenReturn(result);
        translateViewPresenter.requestTranslation(true);

        verify(translateView).hideResult();
        verify(translateView).hideError();
        verify(translateView).showLoading();

        testScheduler.triggerActions();
        verify(translateView, never()).showResult(TEXT);
    }
}