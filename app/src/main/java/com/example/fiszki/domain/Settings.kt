package com.example.fiszki.domain

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.fiszki.domain.usecase.UpdateInstallationStatusUseCase

data class Settings(
    val nativeLanguage: Boolean,
    val installationStatus: Boolean,
    val deleteAfterFive: Boolean
) {
    companion object {
        val DEFAULT_NATIVE_LANGUAGE = true

        val DEFAULT_INSTALLATION_STATUS = false

        val DELETE_AFTER_FIVE = true
    }
}