package com.example.fiszki.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.fiszki.domain.Settings
import com.example.fiszki.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context

) : SettingsRepository {

    val NATIVE_LANGUAGE = booleanPreferencesKey("native_language")
    val INSTALLATION_STATUS = booleanPreferencesKey("installation_status")
    val DELETE_AFTER_FIVE = booleanPreferencesKey("delete_after_five")



    override fun getSettings(): Flow<Settings> = context.dataStore.data.map { preferences ->
        val nativeLanguage = preferences[NATIVE_LANGUAGE] ?: Settings.DEFAULT_NATIVE_LANGUAGE
        val installationStatus = preferences[INSTALLATION_STATUS] ?: Settings.DEFAULT_INSTALLATION_STATUS
        val deleteAfterFive = preferences[DELETE_AFTER_FIVE] ?: Settings.DELETE_AFTER_FIVE
        Settings(
            nativeLanguage = nativeLanguage,
            installationStatus = installationStatus,
            deleteAfterFive = deleteAfterFive
        )
    }

    override suspend fun updateLanguage(native: Boolean) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[NATIVE_LANGUAGE] = native
            }
        }
    }

    override suspend fun updateInstallationStatus(installed: Boolean) {
        Log.d("Test translatora", "no jest w ustawieniach i chce zmienić na $installed")
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[INSTALLATION_STATUS] = installed
            }
        }
    }

    override suspend fun deleteAfterFive(delete: Boolean) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[INSTALLATION_STATUS] = delete
            }
        }
    }
}