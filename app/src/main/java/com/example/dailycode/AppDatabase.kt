package com.example.dailycode.data


import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailycode.Card
import com.example.dailycode.CardsDao
import com.example.dailycode.ClaimedCoupon
import com.example.dailycode.ClaimedCouponDao
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@Database(entities = [Card::class, ClaimedCoupon::class], version = 14)
abstract class AppDatabase : RoomDatabase() {
    abstract fun claimedCouponDao(): ClaimedCouponDao
    abstract fun cardDao(): CardsDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "claimed_coupons_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

