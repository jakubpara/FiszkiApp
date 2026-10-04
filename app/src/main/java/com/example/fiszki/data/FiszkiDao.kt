package com.example.fiszki.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fiszki.domain.Fiszka

@Dao
interface FiszkiDao {

    @Query("SELECT * FROM fiszka WHERE id == :id")
     suspend fun getCurrentCard(id: Int): FiszkaDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(FiszkaDbModel : FiszkaDbModel)

    @Query("DELETE FROM fiszka WHERE id == :id")
    suspend fun deleteCard(id: Int)

    @Query("SELECT * FROM fiszka ORDER BY RANDOM() LIMIT 1")
    suspend fun getNextExample(): FiszkaDbModel?

    @Query("Select * FROM fiszka")
   fun getAllCards():List<FiszkaDbModel>

    @Query("UPDATE fiszka SET counter = :counter WHERE id == :id")
    suspend fun changeCounter(id:Int, counter: Int)
}