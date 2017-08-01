package com.avempra.inventorymanager.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by shres on 8/1/2017.
 */

public class DbBitmapUtility {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static Bitmap returnResizedBitmap(String path, ImageView targetImageView) {
        // Get the dimensions of the View
        int targetW = targetImageView.getWidth();
        int targetH = targetImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        // Determine how much to scale down the image
        int scaleFactor = Math.min(targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return bitmap;
    }
}
