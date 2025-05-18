package com.example.dailycode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CardsDao {
    @Insert
    suspend fun insertCard(card: Card)

    @Query("SELECT * FROM cards")
    fun getAllCards() : List<Card>

    @Delete
    suspend fun delete(card: Card)

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: Int): Card?

}