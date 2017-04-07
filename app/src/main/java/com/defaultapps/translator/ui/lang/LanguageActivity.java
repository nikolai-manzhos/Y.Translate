package com.defaultapps.translator.ui.lang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.defaultapps.translator.R;
import com.defaultapps.translator.data.local.sp.SharedPreferencesManager;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.utils.Global;

import javax.inject.Inject;

//TODO: Implement list for Target and Source languages
public class LanguageActivity extends BaseActivity {

//    @Inject
//    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
//        getActivityComponent().inject(this); //TEMPORARY!

        Intent receivedIntent = getIntent();
        String receivedString = receivedIntent.getStringExtra(Global.SOURCE_OR_TARGET);
        if (savedInstanceState == null) {
            if (receivedString.equals("source")) {
                getSupportActionBar().setTitle("Source language");
                selectFragment(LanguageSelectionViewImpl.newInstance(receivedString));
            } else if (receivedString.equals("target")) {
                getSupportActionBar().setTitle("Target language");
                selectFragment(LanguageSelectionViewImpl.newInstance(receivedString));
            }
        }
    }

    private void selectFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, fragment);
        ft.commit();
    }
}
