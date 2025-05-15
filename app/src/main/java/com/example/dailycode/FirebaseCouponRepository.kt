package com.example.dailycode

import android.content.Context
import com.example.dailycode.data.AppDatabase
import com.example.dailycode.data.Coupon
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class FirebaseCouponRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val couponsCollection = firestore.collection("coupons")


    suspend fun getCouponsByCategories(categories: List<String>): List<Coupon> {
        val querySnapshot = couponsCollection
            .whereIn("category", categories)
            .whereEqualTo("isActive", true)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { it.toObject(Coupon::class.java) }
    }


    suspend fun getRandomCouponByCategories(
        categories: List<String>,
        claimedCoupons: List<Coupon>
    ): Coupon? {
        val allCoupons = getCouponsByCategories(categories)
            .filter { coupon -> claimedCoupons.none { it.id == coupon.id } }

        if (allCoupons.isEmpty()) return null

        // Учитываем вес (weight)
        val totalWeight = allCoupons.sumOf { it.weight }
        val randomValue = Random.nextDouble() * totalWeight

        var cumulativeWeight = 0.0
        for (coupon in allCoupons) {
            cumulativeWeight += coupon.weight
            if (randomValue <= cumulativeWeight) {
                return coupon
            }
        }

        // На всякий случай вернуть последний
        return allCoupons.last()
    }
    suspend fun claimCouponLocally(context: Context, coupon: Coupon) {
        val db = AppDatabase.getDatabase(context)
        val claimedCouponDao = db.claimedCouponDao()

        val claimedCoupon = ClaimedCoupon(
            storeName = coupon.storeName,
            category = coupon.category,
            description = coupon.description,
            imageUrl = coupon.imageUrl,
            isClaimed = true,
            isActive = coupon.isActive,
            weight = coupon.weight
        )

        claimedCouponDao.insert(claimedCoupon)
    }
}