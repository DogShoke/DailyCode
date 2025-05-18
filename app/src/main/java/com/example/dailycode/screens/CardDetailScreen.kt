package com.example.dailycode.screens

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.Card
import com.example.dailycode.data.AppDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer

@Composable
fun CardDetailsScreen(cardId: Int, navController: NavController) {
    val context = LocalContext.current
    var card by remember { mutableStateOf<Card?>(null) }

    LaunchedEffect(cardId) {
        val db = AppDatabase.getDatabase(context)
        card = db.cardDao().getCardById(cardId)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        card?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Магазин: \n ${it.storeName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(50.dp))
                it.cardNumber?.let { number ->
                    BarcodeImage(number)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Номер карты: ${it.cardNumber}",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center)
            }
        }
    }
}


@Composable
fun BarcodeImage(data: String) {
    val bitmap = remember(data) {
        val writer = Code128Writer()
        val bitMatrix = writer.encode(data, BarcodeFormat.CODE_128, 600, 300)
        val bmp = Bitmap.createBitmap(600, 300, Bitmap.Config.ARGB_8888)
        for (x in 0 until 600) {
            for (y in 0 until 300) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Штрихкод",
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    )
}

