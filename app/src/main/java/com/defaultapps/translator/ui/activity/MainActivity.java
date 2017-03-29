package com.defaultapps.translator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.adapter.MainTabAdapter;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.custom.NonSwipeableViewPager;
import com.defaultapps.translator.ui.fragment.FavoritesViewImpl;
import com.defaultapps.translator.ui.fragment.HistoryViewImpl;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.mainViewPager)
    NonSwipeableViewPager viewPager;

    MainTabAdapter mainTabAdapter;
    MenuItem prevMenuItem;


    @State
    int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainTabAdapter);
        viewPager.setPagingEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Log.d("MainActiity", String.valueOf(menuItem.getItemId()));
            selectItem(menuItem);
            return true;
        });
        if (savedInstanceState == null) {
            selectItem(bottomNavigationView.getMenu().getItem(0));
        }
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
