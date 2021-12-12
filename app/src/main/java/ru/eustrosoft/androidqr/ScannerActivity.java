package ru.eustrosoft.androidqr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.model.ScanItem;
import ru.eustrosoft.androidqr.model.ScanItemLab;

public class ScannerActivity extends Activity {
    public static final int REQUEST_CODE = 0x0000c0de;
    private static final String ARG_SCAN_ITEM_ID = "scan_item_id";
    private ScanItem scanItem;
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private StartUpManager startUpManager;
    private boolean needToBib = false;
    private boolean isPause = false;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                return;
            }
            lastText = result.getText();
            showResult(lastText);
            createScanItem(lastText);

            if (needToBib)
                beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Collection<BarcodeFormat> formats = new ArrayList();
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.DATA_MATRIX);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("");
        barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    onResume();
                } else {
                    onPause();
                }
            }
        });
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
    }

    private void createScanItem(String text) {
        UUID scanItemId = UUID.randomUUID();
        scanItem = new ScanItem(scanItemId);
        scanItem.setText(text);
        ScanItemLab.get(getApplicationContext()).addScanItem(scanItem);
    }

    private void init() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        startUpManager = new StartUpManager(getApplicationContext());
        startUpManager.startupMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
        isPause = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    protected void showResult(String text) {
        onPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );
        builder.setTitle("Result");
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Скопировать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Текст скопирован : " + text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (text.startsWith("http://") || text.startsWith("https://")) {
            builder.setNeutralButton("Перейти по ссылке", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                    startActivity(browserIntent);
                }
            });
        }
        builder.show();
    }

    public void showPopupWindow(String searchedText) {
        onPause();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        int width = 500;
        int height = 300;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        TextView textView = (TextView) popupView.findViewById(R.id.text_view_id);
        textView.setLinkTextColor(Color.WHITE);
        textView.setText(searchedText);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clipData = ClipData.newPlainText("text", searchedText);
                Toast.makeText(getApplicationContext(), "Text Copied! : " + searchedText,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
