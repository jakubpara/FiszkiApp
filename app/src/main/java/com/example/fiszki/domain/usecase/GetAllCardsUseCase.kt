package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.repository.FiszkiRepository
import javax.inject.Inject

class GetAllCardsUseCase @Inject constructor(
   private val repository: FiszkiRepository
){
    suspend operator fun invoke():List<Fiszka>{
        return repository.getAllCards()
    }

}