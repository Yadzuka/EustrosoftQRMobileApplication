package ru.eustrosoft.androidqr.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.eustrosoft.androidqr.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView text = (TextView) findViewById(R.id.about_text);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}