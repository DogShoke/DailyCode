package com.example.dailycode.data

data class Coupon(
    val id: String = "",
    val storeName: String = "",
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val weight: Double = 0.5
)
