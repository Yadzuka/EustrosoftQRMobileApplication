package ru.eustrosoft.androidqr.ui.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import ru.eustrosoft.androidqr.R;

public class BrowserActivity extends AppCompatActivity {
    private static final String EXTRA_REFERENCE = "ru.eustrosoft.reference";

    private WebView webView;

    public static Intent newIntent(Context packageContext, String scanText) {
        Intent intent = new Intent(packageContext, BrowserActivity.class);
        intent.putExtra(EXTRA_REFERENCE, scanText);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        webView = findViewById(R.id.browser_window);
        webView.setWebViewClient(new WebViewClient()); // Adds ability to serf through pages

        String reference = (String) getIntent().getSerializableExtra(EXTRA_REFERENCE);
        if (reference != null)
            showSystemBrowserItem(reference);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showSystemBrowserItem(String reference) {
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(reference);
    }
}