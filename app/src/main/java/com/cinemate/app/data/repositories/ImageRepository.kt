package com.cinemate.app.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageRepository(
    private val context: Context,
    private val db: FirebaseFirestore
) {

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "cinemateapp",
            "api_key" to "458396539488896",
            "api_secret" to "_dqhuudlJaL1IZppWf6Q8mW9g_k"
        )
    )

    suspend fun uploadImage(
        imagePath: String,
        documentId: String,
        collectionName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Transformar a imagem para Bitmap
                val originalBitmap = BitmapFactory.decodeFile(imagePath)

                // Converter para WebP
                val webpBitmap = convertToWebP(originalBitmap)

                // Fazer upload para o Cloudinary
                val byteArrayOutputStream = ByteArrayOutputStream()
                webpBitmap.compress(Bitmap.CompressFormat.WEBP, 80, byteArrayOutputStream)
                val webpBytes = byteArrayOutputStream.toByteArray()

                val uploadResult = cloudinary.uploader().upload(
                    webpBytes,
                    ObjectUtils.asMap("resource_type", "image")
                )

                val imageUrl = uploadResult["secure_url"] as String

                saveImageUrlToFirestore(collectionName, documentId, imageUrl)

                imageUrl
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // Método para salvar a URL da imagem no Firestore
    private suspend fun saveImageUrlToFirestore(collectionName: String, documentId: String, imageUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                db.collection(collectionName)
                    .document(documentId)
                    .update("imagem_url", imageUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Método para converter qualquer Bitmap para WebP
    private fun convertToWebP(bitmap: Bitmap): Bitmap {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
