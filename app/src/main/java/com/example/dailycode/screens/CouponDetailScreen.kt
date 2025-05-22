package com.example.dailycode.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.FirebaseCouponRepository
import com.example.dailycode.R
import com.example.dailycode.data.AppDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailScreen(
    navController: NavController,
    storeName: String,
    category: String,
    description: String,
    code: String,
    imageUrl: String
) {
    val coroutineScope = rememberCoroutineScope()
    val barcodeBitmap = remember { generateBarcodeBitmap(code) }
    val context = LocalContext.current
    val imageResId = remember(imageUrl) {
        context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
    }
    val repo = FirebaseCouponRepository()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                        "Детали купона",
                        fontWeight = FontWeight(700),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }},
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier
                        .padding(padding)
                        .width(360.dp)
                        .height(600.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            //.padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                        Text(
                            text = storeName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = category.uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF34C924),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = description,
                            fontSize = 18.sp,
                            color = Color.Black.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(60.dp))

                        // Штрихкод
                        barcodeBitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Barcode",
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Код: ${code}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(40.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val claimedCoupon = repo.getClaimedCouponByDescription(context, description)
                                    repo.deleteClaimedCoupon(context, claimedCoupon!!)
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color( 0xFF7AC87A),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        ){
                            Text(
                                "Удалить",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                                //color = Color(0xFFDEF3E0)
                            )
                        }
                    }
                }
            }
        }
    )
}

fun generateBarcodeBitmap(code: String): Bitmap? {
    return try {
        val width = 600
        val height = 200
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(code, BarcodeFormat.CODE_128, width, height, null)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb())
            }
        }
        bmp
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}