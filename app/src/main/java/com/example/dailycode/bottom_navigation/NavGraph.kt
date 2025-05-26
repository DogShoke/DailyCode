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
            route = "news_detail/{name}/{date}/{description}/{image}/{category}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val descriptionFull = backStackEntry.arguments?.getString("descriptionFull") ?: ""
            val image = backStackEntry.arguments?.getString("image") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""

            NewsDetailScreen(
                navController = navHostController,
                descriptionFull = descriptionFull,
                name = name,
                date = date,
                image = image,
                category = category
            )
        }

    }
}