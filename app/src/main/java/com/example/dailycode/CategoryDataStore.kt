package com.example.dailycode

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object CategoryDataStore {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
    private val CATEGORY_KEY = stringSetPreferencesKey("selected_categories")

    suspend fun saveCategories(context: Context, categories: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[CATEGORY_KEY] = categories.toSet()
        }
    }

    suspend fun loadCategories(context: Context): List<String> {
        val prefs = context.dataStore.data.first()
        return prefs[CATEGORY_KEY]?.toList() ?: emptyList()
    }
}