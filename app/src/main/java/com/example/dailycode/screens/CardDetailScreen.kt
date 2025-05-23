package com.example.dailycode.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.Card as CardEntity
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CardDetailsScreen(cardId: Int, navController: NavController) {
    val context = LocalContext.current
    var card by remember { mutableStateOf<CardEntity?>(null) }

    LaunchedEffect(cardId) {
        val db = AppDatabase.getDatabase(context)
        card = db.cardDao().getCardById(cardId)
    }

    card?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 110.dp, start = 16.dp, end = 16.dp, top = 55.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = it.storeName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        it.cardNumber?.let { number ->
                            BarcodeImage(number)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Номер карты: $number",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color( 0xFF7AC87A)),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 16.dp,
                            pressedElevation = 8.dp,
                            disabledElevation = 0.dp
                        ),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                AppDatabase.getDatabase(context).cardDao().delete(it)
                                launch(Dispatchers.Main) {
                                    Toast.makeText(context, "Карта удалена", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            }
                        },
                    ) {
                        Text("Удалить карту",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth().
                        height(50.dp),
                        border = BorderStroke(1.dp, Color(0xFF7AC87A)), // цвет обводки
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF7AC87A) // цвет текста
                        ),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Назад")
                    }
                }
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
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Штрихкод",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
