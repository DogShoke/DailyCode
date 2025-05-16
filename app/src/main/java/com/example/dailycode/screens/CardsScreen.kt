package com.example.dailycode.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Insert
import com.example.dailycode.Card
import com.example.dailycode.CardsDao

import com.example.dailycode.CardsDb
import com.example.dailycode.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CardsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember(context) { AppDatabase.getDatabase(context) }
    val cardDao = remember { db.cardDao() }

    var cards by remember { mutableStateOf(emptyList<Card>()) }

    var permissionGranted by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) {
            cardDao.getAllCards()
        }
        cards = result
    }
    if (requestPermission && !permissionGranted) {
        RequestCameraPermission(
            onPermissionGranted = {
                permissionGranted = true
                navController.navigate("barcode_scanner")
            }
        )
        requestPermission = false
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Мои карты",fontWeight = FontWeight(700), modifier = Modifier.padding(16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center, style = TextStyle(fontSize = 24.sp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                items(cards) { card ->
                    Text(
                        text = "Магазин: ${card.storeName}, Номер: ${card.cardNumber}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

      /*  FloatingActionButton(
            onClick = {
                navController.navigate("barcode_scanner")
            },*/
        FloatingActionButton(
            onClick = {
                if (permissionGranted) {
                    navController.navigate("barcode_scanner")
                } else {
                    requestPermission = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}