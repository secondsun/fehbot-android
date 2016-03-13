package com.feedhenry.oauth.oauth_android_app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

public class ImageFromNameMaker {

    public static final int[] COLORS =
            {
                    Color.rgb(0, 0, 180),
                    Color.rgb(175, 13, 102),
                    Color.rgb(146, 248, 70),
                    Color.rgb(255, 200, 47),
                    Color.rgb(255, 118, 0),
                    Color.rgb(185, 185, 185),
                    Color.rgb(235, 235, 222),
                    Color.rgb(100, 100, 100),
                    Color.rgb(255, 255, 0),
                    Color.rgb(55, 19, 112),
                    Color.rgb(255, 255, 150),
                    Color.rgb(202, 62, 94),
                    Color.rgb(205, 145, 63),
                    Color.rgb(12, 75, 100),
                    Color.rgb(255, 0, 0),
                    Color.rgb(175, 155, 50),
                    Color.rgb(0, 0, 0),
                    Color.rgb(37, 70, 25),
                    Color.rgb(121, 33, 135),
                    Color.rgb(83, 140, 208),
                    Color.rgb(0, 154, 37),
                    Color.rgb(178, 220, 205),
                    Color.rgb(255, 152, 213),
                    Color.rgb(0, 0, 74),
                    Color.rgb(175, 200, 74),
                    Color.rgb(63, 25, 12)
            };


    public static BitmapDrawable makeImage(Context context, String name) {
        char firstLetter = name.toUpperCase().toCharArray()[0];
        int index = firstLetter % COLORS.length;
        int color = COLORS[index];

        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, r.getDisplayMetrics());
        int text = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());

        Bitmap image = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(image);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(px);
        paint.setColor(Color.WHITE);


        c.drawColor(color);
        c.drawText(firstLetter  + "", (px-text)/2, px-(px-text)/2, paint);

        return new BitmapDrawable(r, image);

    }

}
