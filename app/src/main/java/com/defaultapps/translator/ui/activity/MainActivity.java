package com.defaultapps.translator.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.defaultapps.translator.R;
import com.defaultapps.translator.ui.base.BaseActivity;
import com.defaultapps.translator.ui.fragment.TranslateViewImpl;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        frameLayout.setOnClickListener(view -> Toast.makeText(this, "Lambdas!", Toast.LENGTH_SHORT).show());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new TranslateViewImpl())
                    .commit();
        }
    }
}
