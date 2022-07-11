package ru.eustrosoft.androidqr.util.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public final class ToastHelper {
    private final static int TOAST_NORMAL_DUR = Toast.LENGTH_SHORT;

    private ToastHelper() {

    }

    public static void toastCenter(Context context, String text) {
        if (text == null) {
            text = "";
        }
        Toast newToast = Toast.makeText(context, text, TOAST_NORMAL_DUR);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        newToast.show();
    }
}
