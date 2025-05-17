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
            .whereEqualTo("active", true)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { it.toObject(Coupon::class.java) }
    }


    suspend fun getRandomCouponByCategories(
        categories: List<String>,
        claimedCouponIds: List<String>
    ): Coupon? {
        val allCoupons = getCouponsByCategories(categories)
            .filter { coupon -> coupon.id !in claimedCouponIds }

        if (allCoupons.isEmpty()) return null

        val totalWeight = allCoupons.sumOf { it.weight }
        val randomValue = Random.nextDouble() * totalWeight

        var cumulativeWeight = 0.0
        for (coupon in allCoupons) {
            cumulativeWeight += coupon.weight
            if (randomValue <= cumulativeWeight) {
                return coupon
            }
        }

        return allCoupons.last()
    }


    suspend fun claimCouponLocally(context: Context, coupon: Coupon) {
        val db = AppDatabase.getDatabase(context)
        val claimedCouponDao = db.claimedCouponDao()

        val claimedCoupon = ClaimedCoupon(
            id = coupon.id,
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
    /*suspend fun getOrGenerateCouponForDate(
        context: Context,
        date: String,
        selectedCategories: List<String>,
        claimedCouponIds: List<String>
    ): Coupon? {
        // Проверим, есть ли купон на эту дату
        val storedCoupon = CouponHistoryDataStore.getCouponForDate(context, date)
        if (storedCoupon != null) return storedCoupon

        // Сгенерируем новый, если его нет
        val newCoupon = getRandomCouponByCategories(selectedCategories, claimedCouponIds)
        if (newCoupon != null) {
            CouponHistoryDataStore.saveCouponForDate(context, date, newCoupon)
        }
        return newCoupon
    }
*/

