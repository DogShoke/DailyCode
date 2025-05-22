package com.example.dailycode

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "claimed_coupons")
data class ClaimedCoupon(
    @PrimaryKey()
    val id: String,
    val storeName: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val isClaimed: Boolean = true,
    val isActive: Boolean = true,
    val weight: Double = 1.0,
    val code: String
)
