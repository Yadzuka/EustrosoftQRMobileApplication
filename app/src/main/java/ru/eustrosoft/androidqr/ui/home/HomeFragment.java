package ru.eustrosoft.androidqr.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.ScanItem;
import ru.eustrosoft.androidqr.model.ScanItemLab;
import ru.eustrosoft.androidqr.ui.qr.ScannerActivity;
import ru.eustrosoft.androidqr.util.qr.QRDecoder;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_LOAD_FROM_GALLERY = 2;
    private Button scanButton;
    private Button insertButton;
    private Button galleryPickButton;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        scanButton = root.findViewById(R.id.start_scan);
        insertButton = root.findViewById(R.id.insert_text);
        galleryPickButton = root.findViewById(R.id.scan_from_gallery);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ScannerActivity.class);
            startActivity(intent);
        });

        insertButton.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add new qr text to history");
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String text = input.getText().toString();
                addScanItem(text);
                ToastHelper.toastCenter(getContext(), "New scanned item was added to history. \nYou can see in in correspondence tab.");
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        galleryPickButton.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_LOAD_FROM_GALLERY

            );
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(
                        selectedImage,
                        filePathColumn,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                try {
                    String placeholderTextNotFound = getText(R.string.gallery_item_not_scanned).toString();
                    String decodedText = QRDecoder.decodeFromImage(picturePath, placeholderTextNotFound);
                    if (decodedText.equals(placeholderTextNotFound)) {
                        ToastHelper.toastCenter(getContext(), placeholderTextNotFound);
                        return;
                    }
                    clipData = ClipData.newPlainText("text", decodedText);
                    clipboardManager.setPrimaryClip(clipData);
                    addScanItem(decodedText);
                    decodedText = decodedText + ": text copied!";
                    ToastHelper.toastCenter(getContext(), decodedText);
                } catch (FileNotFoundException ex) {
                    ToastHelper.toastCenter(getContext(), ex.getMessage());
                }
            }
        } catch (Exception ex) {
            ToastHelper.toastCenter(getContext(), ex.getMessage());
        }
    }

    private void addScanItem(String text) {
        ScanItem newItem = new ScanItem();
        newItem.setText(text);
        ScanItemLab.get(getContext()).addScanItem(
                newItem
        );
    }
}