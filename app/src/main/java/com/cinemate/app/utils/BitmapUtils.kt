package com.cinemate.app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun convertToWebP(bitmap: Bitmap, quality: Int = 100): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
        return outputStream.toByteArray()
    }

    fun decodeFileToBitmap(filePath: String): Bitmap {
        return BitmapFactory.decodeFile(filePath)
    }
}
