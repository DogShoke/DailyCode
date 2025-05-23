package com.example.dailycode.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailycode.Card
import com.example.dailycode.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanCardScreen(navController: NavController, scannedCardNumber: String) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var storeName by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // TopBar
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
                    onClick = {  navController.navigate("cards") },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
                Text(
                    "Сохранение карты",
                    fontWeight = FontWeight(700),
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text("Название магазина") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = scannedCardNumber,
                onValueChange = {},
                label = { Text("Номер карты (из QR/штрихкода)") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color( 0xFF7AC87A),
                    disabledContainerColor = androidx.compose.ui.graphics.Color.Gray,
                    contentColor = androidx.compose.ui.graphics.Color.White,
                    disabledContentColor = androidx.compose.ui.graphics.Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ),
                onClick = {
                    val card = Card(
                        storeName = storeName,
                        cardNumber = scannedCardNumber
                    )
                    coroutineScope.launch {
                        db.cardDao().insertCard(card)
                        saved = true
                        Toast.makeText(context, "Карта cохранена", Toast.LENGTH_SHORT).show()
                        navController.navigate("cards")
                    }

                },
                enabled = storeName.isNotBlank()
            ) {
                Text("Сохранить",
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp)
            }
        }
    }
}

@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Требуется разрешение на камеру", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}