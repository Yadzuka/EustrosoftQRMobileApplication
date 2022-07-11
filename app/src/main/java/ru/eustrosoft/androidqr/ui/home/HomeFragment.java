package ru.eustrosoft.androidqr.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.ScannerActivity;
import ru.eustrosoft.androidqr.util.qr.QRDecoder;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
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

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScannerActivity.class);
                startActivity(intent);
            }
        });

        galleryPickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                decodedText = decodedText + ": text copied!";
                ToastHelper.toastCenter(getContext(), decodedText);
            } catch (FileNotFoundException ex) {
                ToastHelper.toastCenter(getContext(), ex.getMessage());
            }
        }
    }
}