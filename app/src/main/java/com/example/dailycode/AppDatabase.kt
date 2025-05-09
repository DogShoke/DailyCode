package com.example.dailycode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailycode.Card
import com.example.dailycode.CardsDao
import com.example.dailycode.data.Coupon
import com.example.dailycode.data.CouponDao

@Database(entities = [Coupon::class, Card::class], version = 9)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao
    abstract fun cardDao(): CardsDao
    companion object {
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "coupon_database"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}

