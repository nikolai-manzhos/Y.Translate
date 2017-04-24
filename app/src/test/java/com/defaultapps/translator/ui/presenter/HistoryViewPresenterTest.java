package com.defaultapps.translator.ui.presenter;

import com.defaultapps.translator.data.interactor.HistoryViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.history.HistoryView;
import com.defaultapps.translator.ui.history.HistoryViewPresenter;
import com.defaultapps.translator.ui.history.HistoryViewPresenterImpl;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;

import static io.reactivex.Observable.error;
import static io.reactivex.Observable.just;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 4/22/2017.
 */
public class HistoryViewPresenterTest {
    @Mock
    HistoryViewInteractor interactor;
    @Mock
    HistoryView view;
    @Mock
    RxBus rxBus;

    private HistoryViewPresenterImpl presenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new HistoryViewPresenterImpl(new CompositeDisposable(), interactor, rxBus);
        presenter.onAttach(view);

    }

    @Test
    public void provideHistDataSuccessTest() throws Exception {
        RealmTranslate result = new RealmTranslate();
        List<RealmTranslate> resultList = new ArrayList<>();
        resultList.add(result);
        TestScheduler testScheduler = new TestScheduler();
        Observable<List<RealmTranslate>> resultObserv = just(resultList).subscribeOn(testScheduler);

        when(interactor.provideHistoryData()).thenReturn(resultObserv);
        presenter.requestHistoryItems();

        testScheduler.triggerActions();

        verify(view).receiveResult(resultList);
    }

    @Test
    public void provideHistDataErrorTest() throws Exception {
        RealmTranslate result = new RealmTranslate();
        List<RealmTranslate> resultList = new ArrayList<>();
        resultList.add(result);

        when(interactor.provideHistoryData()).thenReturn(Observable.error(new IllegalStateException()));

        presenter.requestHistoryItems();

        verify(view, never()).receiveResult(resultList);
    }

    @Test
    public void addToFavTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.addToFavorite(entity)).thenReturn(result);

        presenter.addToFav(entity);

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.FAVORITES_UPDATE, true);
        verify(rxBus).publish(Global.FAVORITE_CHANGED, entity);
    }

    @Test
    public void deleteFromFavTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.deleteFromFavorite(entity)).thenReturn(result);

        presenter.deleteFromFav(entity);

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.FAVORITES_UPDATE, true);
        verify(rxBus).publish(Global.FAVORITE_CHANGED, entity);
    }

    @Test
    public void deleteHistDataTest() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.deleteHistoryData()).thenReturn(result);

        presenter.deleteHistoryData();

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.HISTORY_UPDATE, true);
    }

    @Test
    public void selectItemTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.setCurrentParams(entity)).thenReturn(result);

        presenter.selectItem(entity);

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.TRANSLATE_UPDATE, true);
        verify(rxBus).publish(Global.SELECT_TRANSLATE_FRAGMENT, true);
    }

}
