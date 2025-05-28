package com.example.dailycode.bottom_navigation

import com.example.dailycode.R

sealed class BottomItem(val title: String, val iconId: Int, val route: String){
    object Screen1: BottomItem("", R.drawable.news, "news")
    object Screen2:  BottomItem("", R.drawable.home, "home")
    object Screen3: BottomItem("", R.drawable.coupons, "coupons")
}