package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.repository.FiszkiRepository
import com.example.fiszki.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke ( native: Boolean){
        return repository.updateLanguage(native)
    }
}
