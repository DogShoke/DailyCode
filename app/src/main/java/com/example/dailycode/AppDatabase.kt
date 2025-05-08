package com.example.dailycode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailycode.data.Coupon
import com.example.dailycode.data.CouponDao

//1
/*
package com.example.dailycode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Coupon::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
                return Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coupon_database"
                ).build()

        }
    }
}

*/


//2
/*package com.example.dailycode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Coupon::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coupon_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}*/

//3
@Database(entities = [Coupon::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao

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

