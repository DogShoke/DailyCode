package com.example.dailycode

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Card::class],
    version = 1
)
abstract class CardsDb: RoomDatabase() {
    abstract val dao: CardsDao

}