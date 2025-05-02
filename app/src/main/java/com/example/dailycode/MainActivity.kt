package com.example.dailycode

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import com.google.gson.Gson
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            var coupon by remember { mutableStateOf<Coupon?>(null) }

            LaunchedEffect(Unit) {
                val db = AppDatabase.getDatabase(context)
                val couponDao = db.couponDao()

                // Загружаем JSON из assets
                val json = context.assets.open("coupons.json").bufferedReader().use { it.readText() }

                // Парсим JSON в список объектов
                val gson = Gson()
                val couponListType = object : TypeToken<List<Coupon>>() {}.type
                val couponsFromJson: List<Coupon> = gson.fromJson(json, couponListType)

                // Сохраняем купоны в базе данных
                for (coupon in couponsFromJson) {
                    val exists = couponDao.getCouponByDate(coupon.date)
                    if (exists == null) {
                        couponDao.insertCoupon(coupon)
                    }
                }
            }

            val today = remember { LocalDate.now() }
            var selectedDate by remember { mutableStateOf(today) }

            // Загружаем купон для выбранной даты
            LaunchedEffect(selectedDate) {
                val db = AppDatabase.getDatabase(context)
                val couponDao = db.couponDao()
                val couponForSelectedDate = couponDao.getCouponByDate(selectedDate.toString())
                coupon = couponForSelectedDate
            }

            // Отображаем календарь
            Column {
                CalendarRow(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )

                // Отображаем купон для выбранной даты
                coupon?.let {
                    CouponItem(it)
                } ?: run {
                    Text("Нет купонов для выбранной даты")
                }
            }
        }
    }
}

// Функция для отображения информации о купоне
@Composable
fun CouponItem(coupon: Coupon) {
    Column {
        Text("Store: ${coupon.storeName}")
        Text("Category: ${coupon.category}")
        Text("Description: ${coupon.description}")
        // Здесь можно добавить изображение, если оно есть
        // Image(painter = rememberImagePainter(coupon.imageUrl), contentDescription = null)
    }
}
