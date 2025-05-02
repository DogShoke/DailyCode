package com.example.dailycode.data

import androidx.room.*

@Dao
interface CouponDao {
    @Insert
    suspend fun insertCoupon(coupon: Coupon)

    @Query("SELECT * FROM coupons WHERE date = :date")
    suspend fun getCouponByDate(date: String): Coupon?

    @Query("SELECT * FROM coupons")
    suspend fun getAllCoupons(): List<Coupon>

    @Delete
    suspend fun deleteCoupon(coupon: Coupon)
}
