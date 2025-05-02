package com.example.dailycode.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class Coupon(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // Дата в формате "2025-05-01"
    val storeName: String, // Название магазина
    val category: String, // Категория
    val description: String, // Описание купона
    val imageName: String // Имя изображения в drawable (например: "coupon_image_1")
)
