package com.example.fiszki.domain.usecase

import android.util.Log
import com.example.fiszki.domain.repository.FiszkiRepository

import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    val repository: FiszkiRepository
) {
    suspend operator fun invoke(id: Int, input: String, translation: String) {
        Log.d(
            "debugowanko",
            "Jestesmy w usecase sprawdzamy addCard use case działą wgl"
        )
        return repository.addCard(id, input, translation)
    }
}