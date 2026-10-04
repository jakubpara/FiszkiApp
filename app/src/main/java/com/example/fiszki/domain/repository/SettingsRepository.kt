package com.example.fiszki.domain.repository

import androidx.compose.ui.text.font.FontVariation
import com.example.fiszki.domain.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun updateLanguage(native: Boolean)

    suspend fun updateInstallationStatus(installed: Boolean )

    suspend fun deleteAfterFive(delete: Boolean )
}