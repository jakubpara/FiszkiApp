package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.repository.SettingsRepository
import javax.inject.Inject

class DeleteAfterFive@Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke ( delete: Boolean){
        return repository.deleteAfterFive(delete)
    }
}