package com.example.dailycode.bottom_navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailycode.screens.BarcodeScannerScreen
import com.example.dailycode.screens.CardsScreen
import com.example.dailycode.screens.CategorySelectionScreen
import com.example.dailycode.screens.HomeScreen
import com.example.dailycode.screens.MyCouponsScreen
import com.example.dailycode.screens.ScanCardScreen
import com.example.dailycode.screens.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navHostController: NavHostController
){
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){
            HomeScreen(navHostController)
        }
        composable("coupons"){
            MyCouponsScreen()
        }
        composable("settings"){
            SettingsScreen()
        }

        composable("cards") {
            CardsScreen(navHostController)
        }
        composable("scan_card/{cardNumber}") { backStackEntry ->
            val cardNumber = backStackEntry.arguments?.getString("cardNumber") ?: ""
            ScanCardScreen(navHostController, scannedCardNumber = cardNumber)
        }
        composable("barcode_scanner") {
            BarcodeScannerScreen(navHostController)
        }
        composable("select_categories") {
            CategorySelectionScreen(
                navController = navHostController,
                onCategoriesSelected = { selected ->
                }
            )
        }

    }
}