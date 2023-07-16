package ru.eustrosoft.androidqr.ui.qr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.util.qr.QREncoder;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

public class QREncoderFragment extends Fragment {
    private boolean isImageGenerated = false;
    private ClipData clipData;
    private ClipboardManager clipboardManager;
    private ImageView qrImage;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qrencoder, container, false);

        EditText qrText = view.findViewById(R.id.qr_text);
        qrImage = view.findViewById(R.id.qr_image_view);
        Button encodeButton = view.findViewById(R.id.qr_encode_button);
        QREncoder encoder = new QREncoder();
        clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Set russian symbols support
                    qrImage.setImageBitmap(encoder.createQRImage(1024, qrText.getText().toString()));
                    isImageGenerated = true;
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        qrImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isImageGenerated) {
                    try {
                        ContentValues values = new ContentValues(2);
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                        values.put(MediaStore.Images.Media.DATA, saveImageAndGetPath().getAbsolutePath());
                        ContentResolver theContent = getActivity().getContentResolver();
                        Uri imageUri = theContent.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        ClipData theClip = ClipData.newUri(getActivity().getContentResolver(), "Image", imageUri);
                        clipboardManager.setPrimaryClip(theClip);
                        ToastHelper.toastCenter(getContext(), "Image was copied to clipboard.");
                        return true;
                    } catch (Exception ex) {
                        ToastHelper.toastCenter(getContext(), "Exception while copying image.");
                    }
                }
                return false;
            }
        });

        return view;
    }

    private File saveImageAndGetPath() throws IOException {
        File externalFileDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File finalPath = new File(externalFileDir, "tmp");

        if (!finalPath.exists()) {
            if (!finalPath.mkdir())
                return null;
        }
        String absoluteFilePath = finalPath.getAbsolutePath() + "/encodedQrImage.jpg";
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) qrImage.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        stream.close();
        File tmpFile = new File(absoluteFilePath);
        if (!tmpFile.exists()) {
            tmpFile.createNewFile();
        }
        try (OutputStream os = new FileOutputStream(absoluteFilePath)) {
            os.write(imageInByte);
            os.flush();
        }
        return tmpFile;
    }
}