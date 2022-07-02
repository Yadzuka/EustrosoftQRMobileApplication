package ru.eustrosoft.androidqr.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public final class BitmapTextCreator {

    private BitmapTextCreator() {

    }

    public static Bitmap createBitmapWithText(String text, int textColor, int backgroundColor) {
        int width = 200;
        int height = 100;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setTextSize(14.f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, (width / 2.f), (height / 2.f), paint);
        return bitmap;
    }
}
