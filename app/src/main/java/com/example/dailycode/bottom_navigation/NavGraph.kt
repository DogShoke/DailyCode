package com.example.dailycode.bottom_navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailycode.screens.CardsScreen
import com.example.dailycode.screens.CategorySelectionScreen
import com.example.dailycode.screens.HomeScreen
import com.example.dailycode.screens.MyCouponsScreen
import com.example.dailycode.screens.ScanCardScreen
import com.example.dailycode.screens.SettingsScreen
import com.example.dailycode.ui.screens.CouponScreen

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
        composable("select_categories") {
            CategorySelectionScreen(
                navController = navHostController,
                onCategoriesSelected = { selected ->
                }
            )
        }

    }
}