package com.example.dailycode.data

data class Coupon(
    var id: String = "",
    var storeName: String = "",
    var category: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var isActive: Boolean = true,
    var weight: Double = 1.0,
    val code: String = ""
)
