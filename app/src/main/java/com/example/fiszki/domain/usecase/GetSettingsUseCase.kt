package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.Settings
import com.example.fiszki.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSettingsUseCase @Inject constructor(
    val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings>{
       return repository.getSettings()
    }
}