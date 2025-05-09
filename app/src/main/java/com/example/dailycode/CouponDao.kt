package com.example.dailycode.data

import androidx.room.*

@Dao
interface CouponDao {
    @Query("SELECT * FROM coupons WHERE date = :date LIMIT 1")
    suspend fun getCouponByDate(date: String): Coupon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: Coupon)

    @Update
    suspend fun updateCoupon(coupon: Coupon)

    @Query("SELECT * FROM coupons WHERE is_claimed = 1")
    suspend fun getClaimedCoupons(): List<Coupon>

    @Delete
    suspend fun deleteCoupon(coupon: Coupon)
}
