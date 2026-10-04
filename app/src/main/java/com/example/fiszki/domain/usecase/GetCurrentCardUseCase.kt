package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.repository.FiszkiRepository
import javax.inject.Inject

class GetCurrentCardUseCase @Inject constructor(
    val repository: FiszkiRepository
) {
   suspend operator fun invoke(id: Int): Fiszka {
        return repository.getCurrentCard(id)
    }
}