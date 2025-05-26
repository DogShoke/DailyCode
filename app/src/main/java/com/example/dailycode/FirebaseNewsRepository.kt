package com.example.dailycode

import android.util.Log
import com.example.dailycode.News
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseNewsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val newsCollection = firestore.collection("news")

    suspend fun getAllNews(): List<News> {
        return try {
            val snapshot = newsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(News::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getNewsByCategories(categories: List<String>): List<News> {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("news")
                .whereIn("category", categories) // OK: если category — String
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val news = doc.toObject(News::class.java)?.copy(id = doc.id)
                Log.d("Firebase", "Загружена новость: ${news?.name}")
                news
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Ошибка при загрузке новостей", e)
            emptyList()
        }
    }

}