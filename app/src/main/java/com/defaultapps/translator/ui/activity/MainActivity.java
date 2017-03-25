package com.defaultapps.translator.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new TranslateViewImpl())
                    .commit();
        }

        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.navTranslate).setIcon(
                new IconDrawable(this, MaterialIcons.md_translate)
                .colorRes(R.color.whitePrimary)
        );
        menu.findItem(R.id.navHistory).setIcon(
                new IconDrawable(this, MaterialIcons.md_history)
                .colorRes(R.color.whitePrimary)
        );
        menu.findItem(R.id.navFavorites).setIcon(
                new IconDrawable(this, MaterialIcons.md_favorite)
                .colorRes(R.color.whitePrimary)
        );
    }
}
