package com.example.dailycode.data

import androidx.room.*

/*
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

    @Query("SELECT * FROM coupons WHERE is_claimed = 1")
    suspend fun getClaimedCoupons(): List<Coupon>

    @Update
    suspend fun updateCoupon(coupon: Coupon)

}
*/

@Dao
interface CouponDao {
    // уже были:
    @Query("SELECT * FROM coupons WHERE date = :date LIMIT 1")
    suspend fun getCouponByDate(date: String): Coupon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: Coupon)

    @Update
    suspend fun updateCoupon(coupon: Coupon)

    // новые:
    @Query("SELECT * FROM coupons WHERE is_claimed = 1")
    suspend fun getClaimedCoupons(): List<Coupon>

    @Delete
    suspend fun deleteCoupon(coupon: Coupon)
}
