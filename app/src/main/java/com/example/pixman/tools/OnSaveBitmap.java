package com.example.pixman.tools;

import android.graphics.Bitmap;

/**

 * @version 0.1.2
 * @since 5/21/2018
 */
public interface OnSaveBitmap {
    void onBitmapReady(Bitmap saveBitmap);

    void onFailure(Exception e);
}
