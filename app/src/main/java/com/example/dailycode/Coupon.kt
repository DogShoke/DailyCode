package com.example.dailycode.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class Coupon(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // Дата в формате "2025-05-01"
    val storeName: String,
    val category: String,
    val description: String,
    val imageName: String,
    @ColumnInfo(name = "is_claimed")
    var isClaimed: Boolean = false,
    var activationCode: String? = null
)
