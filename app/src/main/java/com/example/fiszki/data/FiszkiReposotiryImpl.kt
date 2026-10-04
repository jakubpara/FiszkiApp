package com.example.fiszki.data

import android.util.Log
import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.Settings
import com.example.fiszki.domain.repository.FiszkiRepository
import com.example.fiszki.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FiszkiRepositoryImpl @Inject constructor(
    private val fiszkiDao: FiszkiDao,
    private val settingsRepository: SettingsRepository
) : FiszkiRepository {

    override suspend fun getCurrentCard(id: Int): Fiszka {
        return fiszkiDao.getCurrentCard(id).toEntity()
    }

    override suspend fun getNextExample(): Fiszka? {
        return fiszkiDao.getNextExample()?.toEntity()
    }

    override fun checkTranslation(id: Int) {
        TODO("szczerze to tutaj by mozna napisać logike " +
                "gdzie pobierasz ustawienia albo wsm mozna " +
                "to zostawic i robic w prezentacji layer")
    }

    override suspend fun addCard(id: Int, input: String, translation: String) {
        Log.d(
            "debugowanko",
            "to już z poziomu tak zwanego repository impl patrzymy czy addcard działa "
        )
        val newCard = FiszkaDbModel(
            id = 0,
            input = input,
            translation = translation,
            counter = 0


        )
        fiszkiDao.addCard(newCard)
    }

    override suspend fun deleteCard(id: Int) {
        return fiszkiDao.deleteCard(id)
    }

    override suspend fun dontRemember(id: Int) {
        val beforeChange = fiszkiDao.getCurrentCard(id)
        val newCounter = beforeChange.counter - 1
        fiszkiDao.changeCounter(id, newCounter)
    }

    override fun getAllCards(): List<Fiszka> {
        val fiszkiList = fiszkiDao.getAllCards()
        val newListOfEntity = mutableListOf<Fiszka>()
        fiszkiList.forEach {
        newListOfEntity.add(it.toEntity())}
        return newListOfEntity
    }



    override suspend fun iKnowIt(id: Int) {

        val settings = settingsRepository.getSettings().first() // okej to first to mi chat wymyślił
        val delete = settings.deleteAfterFive
        val beforeChange = fiszkiDao.getCurrentCard(id)
        val newCounter = beforeChange.counter + 1
        if (delete && newCounter >= 5) {
            fiszkiDao.deleteCard(id)
        }else {
            fiszkiDao.changeCounter(id, newCounter)
        }
    }
}