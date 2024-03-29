package ru.eustrosoft.androidqr.ui.history;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.ScanItem;
import ru.eustrosoft.androidqr.model.ScanItemDAO;
import ru.eustrosoft.androidqr.ui.browser.BrowserActivity;
import ru.eustrosoft.androidqr.util.BitmapTextCreator;
import ru.eustrosoft.androidqr.util.qr.QREncoder;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedDate;

public class HistoryItemActivity extends AppCompatActivity {
    private static final String EXTRA_SCAN_ITEM_ID = "ru.eustrosoft.scanitemid";

    private TextView textTextView;
    private TextView dateTextView;
    private ImageView imageView;
    private Button systemReferenceButton;
    private Button searchButton;
    private Button copyButton;
    private Button deleteButton;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    public static Intent newIntent(Context packageContext, UUID scanItemId) {
        Intent intent = new Intent(packageContext, HistoryItemActivity.class);
        intent.putExtra(EXTRA_SCAN_ITEM_ID, scanItemId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);
        initElements();

        UUID scanItemId = (UUID) getIntent().getSerializableExtra(EXTRA_SCAN_ITEM_ID);
        if (scanItemId != null) {
            ScanItem scanItem = ScanItemDAO.get(this).getScanItem(scanItemId);
            showScanItemData(scanItem);
            try {
                imageView.setImageBitmap(new QREncoder().createQRImage(256, scanItem.getText()));
            } catch (WriterException e) {
                imageView.setImageBitmap(
                        BitmapTextCreator.createBitmapWithText(
                                getString(R.string.error_loading_qr_code),
                                Color.BLACK,
                                Color.WHITE
                        )
                );
            }
            if (isEustrosoftReference(scanItem.getText()))
                setEustrosoftReferenceBlockVisible(scanItem.getText());

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanItem.getText()));
                        if (browserIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(browserIntent);
                        } else {
                            ToastHelper.toastCenter(getApplicationContext(), "Can not search this text.");
                        }
                    } catch (Exception ex) {
                        ToastHelper.toastCenter(getApplicationContext(), ex.getLocalizedMessage());
                    }
                }
            });

            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clipData = ClipData.newPlainText("text", scanItem.getText());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.label_text_copied),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Are you sure you want to delete this history item?"); //TODO make text string
                    builder.setMessage("");
                    builder.setPositiveButton("Yes", (dialog, id) -> {
                        ScanItemDAO.get(v.getContext())
                                .deleteScanItem(scanItem);
                        ToastHelper.toastCenter(
                                getApplicationContext(),
                                "History item was deleted."
                        );
                        finish();
                    });
                    builder.setNegativeButton("No", (dialog, id) -> {
                        dialog.dismiss();
                    });
                    builder.show();
                }
            });
        }
    }

    private boolean isEustrosoftReference(String text) {
        return text.contains("qr.qxyz.ru");
    }

    private void setEustrosoftReferenceBlockVisible(String reference) {
        findViewById(R.id.layout_scan_item_eustrosoft_reference).setVisibility(View.VISIBLE);
        systemReferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = BrowserActivity.newIntent(getApplicationContext(), reference);
                browser.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                browser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browser);
            }
        });
    }

    private void showScanItemData(ScanItem scanItem) {
        textTextView.setText(scanItem.getText());
        dateTextView.setText(getFormattedDate(scanItem.getDate()));
    }

    private void initElements() {
        textTextView = findViewById(R.id.scan_item_text);
        dateTextView = findViewById(R.id.scan_item_date);
        searchButton = findViewById(R.id.button_scan_item_search);
        copyButton = findViewById(R.id.button_scan_item_copy);
        imageView = findViewById(R.id.scan_item_image_view);
        systemReferenceButton = findViewById(R.id.button_scan_item_eustrosoft_search);
        deleteButton = findViewById(R.id.button_delete_scanned_item);
    }
}
