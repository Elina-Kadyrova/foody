package com.itis.foody.common.db.converters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import javax.inject.Inject

private const val DEFAULT_QUALITY = 100

class ImageToBytesConverter @Inject constructor() {

    fun convertImage(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY, outputStream)
        return outputStream.toByteArray()
    }
}
