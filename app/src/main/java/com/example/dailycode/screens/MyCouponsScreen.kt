package com.example.dailycode.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.dailycode.ClaimedCoupon
import com.example.dailycode.data.AppDatabase

@Composable
fun MyCouponsScreen(navController: NavController) {
    val context = LocalContext.current
    var claimedCoupons by remember { mutableStateOf<List<ClaimedCoupon>>(emptyList()) }
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        db.claimedCouponDao().getAllClaimedCoupons().collect { coupons ->
            claimedCoupons = coupons
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                    Text(
                        "Забранные купоны",
                        fontWeight = FontWeight(700),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                if (claimedCoupons.isEmpty()) {
                    Text(
                        "Нет забранных купонов",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                shape = RoundedCornerShape(12.dp),
                                //colors = CardDefaults.cardColors(containerColor = Color(0xFFDEF3E0))
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 15.dp)
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "${coupon.storeName}",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                                        )

                                        Text(
                                            text = coupon.category.uppercase(),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp,
                                            ),
                                            color = Color(0xFF34C924),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                                        )

                                        Text(
                                            text = coupon.description,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                lineHeight = 24.sp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFB8E3C3),
                                        thickness = 1.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Button(
                                        onClick = {
                                            navController.navigate(
                                                "coupon_detail/${Uri.encode(coupon.storeName)}/${Uri.encode(coupon.category)}/${Uri.encode(coupon.description)}/${Uri.encode(coupon.code)}/${Uri.encode(coupon.imageUrl)}"
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color( 0xFF7AC87A),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                                    ) {
                                        Text(
                                            "Использовать",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            //color = Color(0xFFDEF3E0)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

        }
    )
}
