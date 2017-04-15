package com.defaultapps.translator.ui.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.main.MainTabAdapter;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.custom.NonSwipeableViewPager;
import com.defaultapps.translator.utils.Global;
import com.defaultapps.translator.utils.RxBus;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.mainViewPager)
    NonSwipeableViewPager viewPager;

    @Inject
    RxBus rxBus;

    MainTabAdapter mainTabAdapter;

    @State
    int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainTabAdapter);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            selectItem(menuItem);
            return true;
        });
        MenuItem currentItem;
        if (savedInstanceState != null) {
            currentItem = bottomNavigationView.getMenu().findItem(selectedItem);
        } else {
            currentItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectItem(currentItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(0);
        if (menuItem.getItemId() != selectedItem) {
            selectItem(menuItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectItem(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navTranslate:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navHistory:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navFavorites:
                viewPager.setCurrentItem(2);
                break;
        }
        selectedItem = menuItem.getItemId();

        for (int i = 0; i< bottomNavigationView.getMenu().size(); i++) {
            MenuItem item = bottomNavigationView.getMenu().getItem(i);
            menuItem.setChecked(item.getItemId() == menuItem.getItemId());
        }
    }
}
