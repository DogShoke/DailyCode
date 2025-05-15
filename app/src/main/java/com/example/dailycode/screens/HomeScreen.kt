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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.dailycode.CalendarRow
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

    var selectedCategories by remember { mutableStateOf(listOf("Продукты", "Одежда", "Электроника")) }
    var coupon by remember { mutableStateOf<Coupon?>(null) }



    LaunchedEffect(selectedCategories) {
        val claimedCoupons = claimedCouponDao.getAllClaimedCoupons().first()
        val claimedCouponIds = claimedCoupons.map { it.id }

        coupon = couponRepository.getRandomCouponByCategories(
            selectedCategories,
            claimedCouponIds
        )
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

        val isButtonEnabled = selectedDate == today && coupon != null

        Button(
            onClick = {
                coupon?.let {
                    coroutineScope.launch {
                        couponRepository.claimCouponLocally(context, it)

                        val claimedCouponIds = claimedCouponDao
                            .getAllClaimedCoupons()
                            .first()
                            .map { claimed -> claimed.id }

                        coupon = couponRepository.getRandomCouponByCategories(
                            selectedCategories,
                            claimedCouponIds
                        )
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




