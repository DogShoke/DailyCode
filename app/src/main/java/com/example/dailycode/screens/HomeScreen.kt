package com.example.dailycode.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailycode.CalendarRow
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import com.example.dailycode.data.CouponDao
import com.google.gson.Gson
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var coupon by remember { mutableStateOf<Coupon?>(null) }
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }
    var isInitialized by remember { mutableStateOf(false) }

    // Загрузка JSON и сохранение в базу
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val couponDao = db.couponDao()
        val json = context.assets.open("coupons.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<Coupon>>() {}.type
        val coupons: List<Coupon> = gson.fromJson(json, type)
        for (item in coupons) {
            if (couponDao.getCouponByDate(item.date) == null) {
                couponDao.insertCoupon(item)
            }
        }
        isInitialized = true
    }


    // Получение купона по дате
    LaunchedEffect(selectedDate, isInitialized) {
        if (isInitialized) {
            val db = AppDatabase.getDatabase(context)
            val couponDao = db.couponDao()
            coupon = couponDao.getCouponByDate(selectedDate.toString())
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        CalendarRow(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            today = today
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            selectedDate.isAfter(today) -> {
                Text("Купон пока недоступен. Дождитесь нужной даты.")
            }

            coupon != null -> {
                CouponItem(coupon!!)
            }

            else -> {
                Text("Нет купонов для выбранной даты")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val isButtonEnabled = selectedDate == today && coupon != null && !coupon!!.isClaimed

        Button(
            onClick = {
                if (isButtonEnabled) {
                    coupon?.let {
                        it.isClaimed = true
                        it.activationCode = generateActivationCode()
                        coroutineScope.launch {
                            AppDatabase.getDatabase(context).couponDao().updateCoupon(it)
                        }
                    }
                }
            },
            enabled = isButtonEnabled
        ) {
            Text("ЗАБРАТЬ")
        }
        Button(onClick = { navController.navigate("cards") }) {
            Text("Мои карты")
        }
    }

}

@Composable
fun CouponItem(coupon: Coupon) {
    val context = LocalContext.current
    val imageResId = remember(coupon.imageName) {
        context.resources.getIdentifier(coupon.imageName, "drawable", context.packageName)
    }

    Column {
        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Text("Магазин: ${coupon.storeName}")
        Text("Категория: ${coupon.category}")
        Text("${coupon.description}")
    }
}

fun generateActivationCode(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..8)
        .map { chars.random() }
        .chunked(4)
        .joinToString(" ") { it.joinToString("") }
}


