package ru.eustrosoft.androidqr;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_log);

        Resources resources = getResources();
        String logs = resources.getString(R.string.application_log);

    }
}