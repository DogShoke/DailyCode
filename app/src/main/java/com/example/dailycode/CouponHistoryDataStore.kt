package com.example.dailycode

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailycode.data.Coupon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "coupon_history")


object CouponHistoryDataStore {
    private val gson = Gson()
    private val COUPON_HISTORY_KEY = stringPreferencesKey("coupon_history")

    suspend fun saveCouponForDate(context: Context, date: String, coupon: Coupon) {
        val currentMap = loadCouponMap(context).toMutableMap()
        currentMap[date] = coupon
        val json = gson.toJson(currentMap)
        context.dataStore.edit { prefs ->
            prefs[COUPON_HISTORY_KEY] = json
        }
        if (currentMap.size > 8) {
            val oldestKey = currentMap.keys.sorted().first()
            currentMap.remove(oldestKey)
        }

    }

    suspend fun getCouponForDate(context: Context, date: String): Coupon? {
        val map = loadCouponMap(context)
        return map[date]
    }

    private suspend fun loadCouponMap(context: Context): Map<String, Coupon> {
        val prefs = context.dataStore.data.first()
        val json = prefs[COUPON_HISTORY_KEY] ?: return emptyMap()
        val type = object : TypeToken<Map<String, Coupon>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
    }
}
