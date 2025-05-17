package com.example.dailycode.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.dailycode.CalendarRow
import com.example.dailycode.CategoryDataStore
import com.example.dailycode.CouponHistoryDataStore
import com.example.dailycode.FirebaseCouponRepository
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val claimedCouponDao = remember { db.claimedCouponDao() }

    val couponRepository = remember { FirebaseCouponRepository() }

    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }

    //var selectedCategories by remember { mutableStateOf(emptyList<String>()) }
    var selectedCategories by remember { mutableStateOf(listOf( "Еда",
        "Одежда",
        "Техника",
        "Красота",
        "Дом и ремонт",
        "Развлечения",
        "Спорт",
        "Транспорт",
        "Книги",
        "Путешествия"))}
    var coupon by remember { mutableStateOf<Coupon?>(null) }
    var categoriesLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val savedCategories = CategoryDataStore.loadCategories(context)
        selectedCategories = if (savedCategories.isNotEmpty()) {
            savedCategories
        } else {
            listOf(
                "Еда",
                "Одежда",
                "Техника",
                "Красота",
                "Дом и ремонт",
                "Развлечения",
                "Спорт",
                "Транспорт",
                "Книги",
                "Путешествия"
            )
        }
        categoriesLoaded = true


        val todayStr = today.toString()
        val savedCoupon = CouponHistoryDataStore.getCouponForDate(context, todayStr)
        if (savedCoupon != null) {
            coupon = savedCoupon
        } else {
            val claimedCouponIds = claimedCouponDao.getAllClaimedCoupons().first().map { it.id }
            val newCoupon = couponRepository.getRandomCouponByCategories(selectedCategories, claimedCouponIds)
            if (newCoupon != null) {
                CouponHistoryDataStore.saveCouponForDate(context, todayStr, newCoupon)
                coupon = newCoupon
            }
        }
    }


    LaunchedEffect(selectedDate) {
        if (!categoriesLoaded) return@LaunchedEffect
        val dateStr = selectedDate.toString()
        val savedCoupon = CouponHistoryDataStore.getCouponForDate(context, dateStr)
        coupon = savedCoupon
        Log.d("ToastDebug", "Показ купона: ${savedCoupon?.storeName}")
        Log.d("CouponDebug", "Категории: $selectedCategories")
    }

    var isClaimed by remember { mutableStateOf(false) }

    LaunchedEffect(coupon) {
        if (coupon != null) {
            val claimedCoupons = claimedCouponDao.getAllClaimedCoupons().first()
            isClaimed = claimedCoupons.any { it.id == coupon!!.id }
        } else {
            isClaimed = false
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

        val isButtonEnabled = selectedDate == today && coupon != null && !isClaimed

        Button(
            onClick = {
                coupon?.let {
                    coroutineScope.launch {
                        couponRepository.claimCouponLocally(context, it)
                        if (coupon != null) {
                            val claimedCoupons = claimedCouponDao.getAllClaimedCoupons().first()
                            isClaimed = claimedCoupons.any { it.id == coupon!!.id }
                        } else {
                            isClaimed = false
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
        Button(
            onClick = { navController.navigate("select_categories") },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Выбрать категории")
        }

    }



}

@Composable
fun CouponItem(coupon: Coupon) {
    Column {
        Image(
            painter = rememberImagePainter(coupon.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Магазин: ${coupon.storeName}")
        Text("Категория: ${coupon.category}")
        Text("Описание: ${coupon.description}")
    }
}




