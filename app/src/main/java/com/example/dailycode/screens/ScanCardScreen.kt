package com.example.dailycode.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailycode.Card
import com.example.dailycode.data.AppDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanCardScreen(navController: NavController, scannedCardNumber: String) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var storeName by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Сохранение карты магазина", modifier = Modifier.padding(bottom = 16.dp))

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
            onClick = {
                val card = Card(
                    storeName = storeName,
                    cardNumber = scannedCardNumber
                )
                coroutineScope.launch {
                    db.cardDao().insertCard(card)
                    saved = true
                }
            },
            enabled = storeName.isNotBlank()
        ) {
            Text("Сохранить")
        }

        if (saved) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Карта успешно сохранена!")
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