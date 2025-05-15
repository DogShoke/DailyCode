package com.example.dailycode.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailycode.ClaimedCoupon
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import kotlinx.coroutines.launch

@Composable
fun MyCouponsScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var claimedCoupons by remember { mutableStateOf<List<ClaimedCoupon>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        claimedCoupons = db.claimedCouponDao().getAllClaimedCoupons()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Забранные купоны")

        claimedCoupons.forEach { claimedCoupon ->
            val coupon = claimedCoupon

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Магазин: ${coupon.storeName}")
                    Text("Категория: ${coupon.category}")
                    Text("Описание: ${coupon.description}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            val db = AppDatabase.getDatabase(context)
                            db.claimedCouponDao().deleteClaimedCoupon(claimedCoupon)
                            claimedCoupons = db.claimedCouponDao().getAllClaimedCoupons()
                        }
                    }) {
                        Text("Удалить")
                    }
                }
            }
        }

        if (claimedCoupons.isEmpty()) {
            Text("Нет забранных купонов", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}
