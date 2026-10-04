package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.repository.FiszkiRepository
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(
    private val repository: FiszkiRepository
) {
    suspend operator fun invoke(id: Int) {
        return repository.deleteCard(id)
    }
}