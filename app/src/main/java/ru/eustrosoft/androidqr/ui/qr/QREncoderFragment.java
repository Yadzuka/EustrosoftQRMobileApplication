package ru.eustrosoft.androidqr.ui.qr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.util.qr.QREncoder;

public class QREncoderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qrencoder, container, false);

        EditText qrText = view.findViewById(R.id.qr_text);
        ImageView qrImage = view.findViewById(R.id.qr_image_view);
        Button encodeButton = view.findViewById(R.id.qr_encode_button);
        QREncoder encoder = new QREncoder();

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Set russian symbols support
                    qrImage.setImageBitmap(encoder.createQRImage(1024, qrText.getText().toString()));
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}