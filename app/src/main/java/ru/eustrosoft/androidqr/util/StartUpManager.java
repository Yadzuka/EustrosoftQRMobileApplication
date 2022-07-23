package ru.eustrosoft.androidqr.util;

import android.content.Context;
import android.widget.Toast;

public class StartUpManager {
    private final static String WELCOME_MESSAGE = "Welcome to the EustrosoftQR code scanner mobile application!";
    private Context appContext;

    protected StartUpManager() {
    }

    public StartUpManager(Context context) {
        this.appContext = context;
    }

    public void setStartupVariables() {

    }

    public void startupMessage() {
        Toast.makeText(appContext, WELCOME_MESSAGE, Toast.LENGTH_LONG).show();
    }
}
