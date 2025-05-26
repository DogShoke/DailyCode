package com.example.dailycode.bottom_navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dailycode.ClaimedCoupon
import com.example.dailycode.News
import com.example.dailycode.screens.BarcodeScannerScreen
import com.example.dailycode.screens.CardDetailsScreen
import com.example.dailycode.screens.CardsScreen
import com.example.dailycode.screens.CategorySelectionScreen
import com.example.dailycode.screens.CouponDetailScreen
import com.example.dailycode.screens.HomeScreen
import com.example.dailycode.screens.MyCouponsScreen
import com.example.dailycode.screens.NewsDetailScreen
import com.example.dailycode.screens.NewsScreen
import com.example.dailycode.screens.ScanCardScreen
import com.example.dailycode.screens.SettingsScreen
import com.google.gson.Gson

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
            MyCouponsScreen(navHostController)
        }
        composable("news"){
            NewsScreen(navHostController)
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

        composable("cardDetails/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")?.toIntOrNull()
            cardId?.let {
                CardDetailsScreen(cardId = it, navController = navHostController)
            }
        }

        composable(
            route = "coupon_detail/{storeName}/{category}/{description}/{code}/{imageUrl}",
            arguments = listOf(
                navArgument("storeName") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storeName = backStackEntry.arguments?.getString("storeName") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""

            CouponDetailScreen(
                navController = navHostController,
                storeName = storeName,
                category = category,
                description = description,
                code = code,
                imageUrl = imageUrl
            )
        }

        composable(
            "news_detail/{newsJson}",
            arguments = listOf(
                navArgument("newsJson") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val newsJson = backStackEntry.arguments?.getString("newsJson")
            val news = Gson().fromJson(newsJson, News::class.java)
            NewsDetailScreen(navHostController, news)
        }
    }
}