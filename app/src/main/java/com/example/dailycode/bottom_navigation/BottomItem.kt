package com.example.dailycode.bottom_navigation

import com.example.dailycode.R

sealed class BottomItem(val title: String, val iconId: Int, val route: String){
    object Screen1: BottomItem("Главный Экран", R.drawable.home, "home")
    object Screen2: BottomItem("Купоны", R.drawable.coupons, "coupons")
    object Screen3: BottomItem("Настройки", R.drawable.settings, "settings")
}