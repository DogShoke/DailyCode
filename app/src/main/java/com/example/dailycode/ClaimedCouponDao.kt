package com.example.dailycode

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ClaimedCouponDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(claimedCoupon: ClaimedCoupon)

    @Delete
    suspend fun delete(claimedCoupon: ClaimedCoupon)

    @Query("SELECT * FROM claimed_coupons")
    fun getAllClaimedCoupons(): Flow<List<ClaimedCoupon>>

    @Query("SELECT EXISTS(SELECT 1 FROM claimed_coupons WHERE id = :couponId)")
    suspend fun isCouponClaimed(couponId: String): Boolean
}
