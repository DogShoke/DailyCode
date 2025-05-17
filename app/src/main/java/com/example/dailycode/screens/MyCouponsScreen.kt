package com.example.dailycode.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycode.ClaimedCoupon
import com.example.dailycode.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun MyCouponsScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var claimedCoupons by remember { mutableStateOf<List<ClaimedCoupon>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        db.claimedCouponDao().getAllClaimedCoupons().collect { coupons ->
            claimedCoupons = coupons
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            "Забранные купоны",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        if (claimedCoupons.isEmpty()) {
            Text(
                "Нет забранных купонов",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(claimedCoupons) { coupon ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Магазин: ${coupon.storeName}")
                            Text("Категория: ${coupon.category}")
                            Text("Описание: ${coupon.description}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                coroutineScope.launch {
                                    val db = AppDatabase.getDatabase(context)
                                    db.claimedCouponDao().deleteClaimedCoupon(coupon)
                                    db.claimedCouponDao().getAllClaimedCoupons().collect { updatedList ->
                                        claimedCoupons = updatedList
                                    }
                                }
                            }) {
                                Text("Удалить")
                            }
                        }
                    }
                }
            }
        }
    }
}
