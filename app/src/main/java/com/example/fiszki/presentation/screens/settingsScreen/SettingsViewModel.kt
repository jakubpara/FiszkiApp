package com.example.fiszki.presentation.screens.settingsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiszki.domain.usecase.DeleteAfterFiveUseCase
import com.example.fiszki.domain.usecase.GetSettingsUseCase
import com.example.fiszki.domain.usecase.InstallTranslatorUseCase
import com.example.fiszki.domain.usecase.UpdateInstallationStatusUseCase
import com.example.fiszki.domain.usecase.UpdateLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateInstallationStatusUseCase: UpdateInstallationStatusUseCase,
    private val deleteAfterFiveUseCase: DeleteAfterFiveUseCase,
    private val installTranslatorUseCase: InstallTranslatorUseCase,
): ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState(false, false,true))
    val  settingsState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            getSettingsUseCase().collect{
                settings ->
                _settingsState.update {
                    SettingsState(
                        nativeLanguage = settings.nativeLanguage,
                        installationStatus = settings.installationStatus,
                        deleteAfterFive = settings.deleteAfterFive
                    )
                }

            }
        }
    }

    fun getSettings(){
        Log.d("Test translatora", "czy sie wywołuje takie gówienko")
        viewModelScope.launch {
            getSettingsUseCase().collect{
                    settings ->
                _settingsState.update {
                    SettingsState(
                        nativeLanguage = settings.nativeLanguage,
                        installationStatus = settings.installationStatus,
                        deleteAfterFive = settings.deleteAfterFive


                    )

                }

            }
        }
    }


    fun updateLanguage(native: Boolean){
        viewModelScope.launch {
            updateLanguageUseCase(native)
        }
    }

    // tego nie ma co dawać bo jak sie zainstaluje to samo sie zmieni xd
    fun updateInstallationStatus(installed: Boolean ){
        viewModelScope.launch {
            updateInstallationStatusUseCase(installed)
        }
    }

    fun installTranslation(){
        viewModelScope.launch {
            installTranslatorUseCase()
        }
    }

    fun deleteAfterFive(delete: Boolean){
        viewModelScope.launch {
            deleteAfterFiveUseCase(delete)
        }
    }

}

data class SettingsState(

    val nativeLanguage: Boolean,
    val installationStatus: Boolean,
    val deleteAfterFive: Boolean
)