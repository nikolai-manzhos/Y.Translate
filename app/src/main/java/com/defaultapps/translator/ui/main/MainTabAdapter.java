package com.defaultapps.translator.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.defaultapps.translator.ui.favorite.FavoritesViewImpl;
import com.defaultapps.translator.ui.history.HistoryViewImpl;
import com.defaultapps.translator.ui.translate.TranslateViewImpl;


public class MainTabAdapter extends FragmentPagerAdapter {

    public MainTabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TranslateViewImpl();
                break;
            case 1:
                fragment = new HistoryViewImpl();
                break;
            case 2:
                fragment = new FavoritesViewImpl();
                break;
            default:
                fragment = new TranslateViewImpl();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
