package com.defaultapps.translator.ui.presenter;


import com.defaultapps.translator.data.interactor.FavoritesViewInteractor;
import com.defaultapps.translator.data.model.realm.RealmTranslate;
import com.defaultapps.translator.ui.favorite.FavoritesView;
import com.defaultapps.translator.ui.favorite.FavoritesViewPresenter;
import com.defaultapps.translator.ui.favorite.FavoritesViewPresenterImpl;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.TestScheduler;

import static io.reactivex.Observable.just;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavoritesViewPresenterTest {
    @Mock
    private FavoritesView view;
    @Mock
    private FavoritesViewInteractor interactor;
    @Mock
    private RxBus rxBus;

    private FavoritesViewPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new FavoritesViewPresenterImpl(new CompositeDisposable(), interactor, rxBus);
        presenter.onAttach(view);
    }

    @Test
    public void requestItemsTest() throws Exception {
        List<RealmTranslate> resultData = provideFavList();
        TestScheduler testScheduler = new TestScheduler();
        Observable<List<RealmTranslate>> result = just(resultData).subscribeOn(testScheduler);
        when(interactor.provideFavoritesData()).thenReturn(result);

        presenter.requestFavoriteItems();

        testScheduler.triggerActions();

        verify(view).hideNoDataView();
        verify(view, never()).showNoDataView();
        verify(view).receiveResult(resultData);
    }

    /**
     * Send empty list so NoDataView should be displayed
     * @throws Exception
     */
    @Test
    public void requestItemsTest2() throws Exception {
        List<RealmTranslate> resultData = provideEmptyFavList();
        TestScheduler testScheduler = new TestScheduler();
        Observable<List<RealmTranslate>> result = just(resultData).subscribeOn(testScheduler);
        when(interactor.provideFavoritesData()).thenReturn(result);

        presenter.requestFavoriteItems();

        testScheduler.triggerActions();

        verify(view).showNoDataView();
        verify(view, never()).hideNoDataView();
        verify(view).receiveResult(resultData);
    }

    @Test
    public void deleteFavoritesTest() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.deleteFavoritesData()).thenReturn(result);

        presenter.deleteFavorites();

        testScheduler.triggerActions();

        verify(rxBus).publish(Global.FAVORITES_UPDATE, true);
        verify(rxBus).publish(Global.HISTORY_UPDATE, true);
        verify(rxBus).publish(Global.FAVORITE_CHANGED, true);
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

    @Test
    public void deleteItemFromFavoritesTest() throws Exception {
        RealmTranslate entity = new RealmTranslate();
        TestScheduler testScheduler = new TestScheduler();
        Observable<Boolean> result = just(true).subscribeOn(testScheduler);
        when(interactor.deleteFavoriteItem(entity)).thenReturn(result);

        presenter.deleteItemFromFavorites(entity);

        testScheduler.triggerActions();

        rxBus.publish(Global.HISTORY_UPDATE, true);
        rxBus.publish(Global.FAVORITE_CHANGED, entity);
    }

    private List<RealmTranslate> provideFavList() {
        List<RealmTranslate> resultList = new ArrayList<>();
        resultList.add(new RealmTranslate());
        resultList.add(new RealmTranslate());
        return resultList;
    }

    private List<RealmTranslate> provideEmptyFavList() {
        return new ArrayList<>();
    }
}
