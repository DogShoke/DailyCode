package com.example.dailycode


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.dailycode.bottom_navigation.MainScreen
import com.example.dailycode.data.Coupon
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import java.util.Calendar
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* fun uploadCouponsFromJson(context: Context) {
            val json = context.assets.open("couponsNew2.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val coupons = gson.fromJson(json, Array<Coupon>::class.java).toList()
            val db = FirebaseFirestore.getInstance()

            for (coupon in coupons) {
                db.collection("coupons").document(coupon.id).set(coupon)
                    .addOnSuccessListener { Log.d("Firestore", "Uploaded: ${coupon.id}") }
                    .addOnFailureListener { e -> Log.w("Firestore", "Error uploading", e) }
            }
        }
        uploadCouponsFromJson(this)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }
        setContent {
            MainScreen()
        }

        fun scheduleDailyNotification(hour: Int, minute: Int, type: String) {
            val now = Calendar.getInstance()
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
            }

            val delay = targetTime.timeInMillis - now.timeInMillis

            val workRequest = PeriodicWorkRequestBuilder<CouponReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("type" to type))
                .addTag("coupon_$type")
                .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "coupon_work_$type",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }

        scheduleDailyNotification(10, 0, "morning")
        scheduleDailyNotification(19, 0, "evening")
    }
}

