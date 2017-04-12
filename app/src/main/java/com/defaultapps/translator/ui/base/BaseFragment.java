package com.defaultapps.translator.ui.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.defaultapps.translator.R;


public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void displayDialog(String title,
                                 String message,
                                 DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.alert_ok, listener)
                .setNegativeButton(R.string.alert_cancel, listener)
                .show();
    }
}
