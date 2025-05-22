package com.example.dailycode


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailycode.bottom_navigation.MainScreen
import com.example.dailycode.data.Coupon
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson


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
        setContent {
            MainScreen()
        }
    }
}
