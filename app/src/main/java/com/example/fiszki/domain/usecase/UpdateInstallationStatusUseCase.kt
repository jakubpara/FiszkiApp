package com.example.fiszki.domain.usecase

import android.util.Log
import com.example.fiszki.data.TranslatorDataSource
import com.example.fiszki.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateInstallationStatusUseCase @Inject constructor(
    val repository: SettingsRepository
) {
    suspend operator fun invoke(installed: Boolean){
        Log.d("Test translatora", " czy doszedł usecase aby zmienic ustawieniach wartosc")
        return repository.updateInstallationStatus(installed)
    }
}