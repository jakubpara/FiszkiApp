package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.repository.FiszkiRepository
import javax.inject.Inject

class CheckTranslationUseCase @Inject constructor(
    private val repository: FiszkiRepository
) {
    operator fun invoke(id: Int) {
        return repository.checkTranslation(id)
    }
}