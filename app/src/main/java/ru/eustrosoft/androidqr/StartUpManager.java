package ru.eustrosoft.androidqr;

import android.content.Context;
import android.widget.Toast;

public class StartUpManager {
    private final static String WELCOME_MESSAGE = "Welcome to the EustrosoftQR code scanner mobile application!";
    private Context appContext;

    protected StartUpManager() {
    }

    protected StartUpManager(Context context) {
        this.appContext = context;
    }

    protected void setStartupVariables() {

    }

    protected void startupMessage() {
        Toast.makeText(appContext, WELCOME_MESSAGE, Toast.LENGTH_LONG).show();
    }
}
