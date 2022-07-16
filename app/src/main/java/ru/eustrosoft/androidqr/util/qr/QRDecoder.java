package ru.eustrosoft.androidqr.util.qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class QRDecoder {
    private QRDecoder() {

    }

    public static String decodeFromImage(String filePath, String placeholderTextNotScanned)
            throws FileNotFoundException {
        if (filePath == null || !(new File(filePath).exists())) {
            throw new FileNotFoundException("File not found on device");
        }
        if (placeholderTextNotScanned == null) {
            placeholderTextNotScanned = "";
        }
        String contents = placeholderTextNotScanned;
        Bitmap bitmapImage = BitmapFactory.decodeStream(new FileInputStream(filePath));

        int[] intArray = new int[bitmapImage.getWidth() * bitmapImage.getHeight()];

        bitmapImage.getPixels(
                intArray,
                0,
                bitmapImage.getWidth(),
                0,
                0,
                bitmapImage.getWidth(),
                bitmapImage.getHeight()
        );

        LuminanceSource source = new RGBLuminanceSource(bitmapImage.getWidth(), bitmapImage.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        } catch (Exception e) {
            Log.e("QRImage", "Error while decoding image from file", e);
        }
        return contents;
    }
}
