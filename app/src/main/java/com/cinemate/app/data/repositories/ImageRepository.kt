package com.cinemate.app.data.repositories

import android.content.Context
import com.cinemate.app.utils.BitmapUtils
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                val originalBitmap = BitmapUtils.decodeFileToBitmap(imagePath)

                val webpBytes = BitmapUtils.convertToWebP(originalBitmap)

                val uploadResult = cloudinary.uploader().upload(
                    webpBytes,
                    ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "imagens_filmes"
                    )
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
}
