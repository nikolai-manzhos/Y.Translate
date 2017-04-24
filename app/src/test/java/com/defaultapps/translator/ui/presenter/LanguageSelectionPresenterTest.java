package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.data.interactor.LanguageViewInteractor;
import com.defaultapps.translator.ui.lang.LanguageSelectionPresenterImpl;
import com.defaultapps.translator.ui.lang.LanguageSelectionView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.TestScheduler;

import static io.reactivex.Observable.just;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 4/24/2017.
 */

public class LanguageSelectionPresenterTest {
    @Mock
    private LanguageSelectionView view;
    @Mock
    private LanguageViewInteractor interactor;

    private LanguageSelectionPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new LanguageSelectionPresenterImpl(new CompositeDisposable(), interactor);
        presenter.onAttach(view);
    }

    @Test
    public void requestLangListTest() throws Exception {
        Map<String, String> resultData = new HashMap<>();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Map<String, String>> result = just(resultData).subscribeOn(testScheduler);
        when(interactor.requestLangList()).thenReturn(result);

        presenter.requestLangList();

        testScheduler.triggerActions();

        verify(view).updateLangList(resultData);
    }

}
