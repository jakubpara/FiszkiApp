package com.example.fiszki.domain.repository

import com.example.fiszki.domain.Fiszka

interface FiszkiRepository {

    suspend fun getCurrentCard(id: Int): Fiszka

    fun checkTranslation(id: Int)

    suspend fun addCard(id: Int, input: String, translation: String)

    suspend fun deleteCard(id: Int)

    suspend fun dontRemember(id: Int)

    suspend fun getNextExample(): Fiszka?

    fun getAllCards():List<Fiszka>

    suspend fun iKnowIt(id:Int)
}