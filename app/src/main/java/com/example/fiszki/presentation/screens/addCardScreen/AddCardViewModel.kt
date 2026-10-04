package com.example.fiszki.presentation.screens.addCardScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.usecase.AddCardUseCase
import com.example.fiszki.domain.usecase.CloseTranslatorUseCase
import com.example.fiszki.domain.usecase.InstallTranslatorUseCase
import com.example.fiszki.domain.usecase.TranslateUseCase
import com.example.fiszki.presentation.screens.ReviewScreen.ReviewingCommand
import com.example.fiszki.presentation.screens.ReviewScreen.ReviewingState
import com.example.fiszki.presentation.screens.addCardScreen.AddCardState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.annotation.Native
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val addCardUseCase: AddCardUseCase,
    private val installTranslatorUseCase: InstallTranslatorUseCase,
    private val translateUseCase: TranslateUseCase,
    private val closeTranslatorUseCase: CloseTranslatorUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<AddCardState>(AddCardState.FirstPageInput())
    val state = _state.asStateFlow()

    fun processCommand(command: AddCardCommand) {
        when (command) {
            is AddCardCommand.AcceptSuggestion -> {
                viewModelScope.launch {

                    if (_state.value is AddCardState.SecondPageInput) {
                        if (command.installed) {
                            val translation =
                                translateUseCase((_state.value as SecondPageInput).nativeText)
                            Log.d("Test translatora", " czy poszło polecenie translateusecase")
                            _state.update { previousState ->
                                AddCardState.SecondPageInput(
                                    nativeText = (_state.value as SecondPageInput).nativeText,
                                    translation,
                                    translation
                                )
                            }


                        } else {

                            installTranslatorUseCase()
                            Log.d(
                                "Test translatora",
                                " czy poszło polecenie installTranslatorUseCase"
                            )
                            _state.update { previousState ->
                                AddCardState.SecondPageInput(
                                    nativeText = (_state.value as SecondPageInput).nativeText,
                                    usersTranslation = (_state.value as SecondPageInput).usersTranslation,
                                    "unknown "
                                )
                            }
                        }
                    }
                }
            }


            is AddCardCommand.InputText -> {
                _state.update { previousState ->
                    when (previousState) {
                        is FirstPageInput -> {
                            if (command.isNative) {
                                previousState.copy(text = command.nativeText)
                            } else {
                                previousState.copy(usersTranslation = command.usersTranslation)
                            }
                        }

                        is SecondPageInput -> {
                            if (command.isNative) {
                                previousState.copy(usersTranslation = command.usersTranslation)
                            } else {
                                previousState.copy(nativeText = command.nativeText)
                            }
                        }

                        else -> previousState
                    }
                }
            }


            AddCardCommand.OnCardClick -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        delay(200.milliseconds)
                        if (previousState is AddCardState.FirstPageInput) {
                            SecondPageInput(
                                nativeText = previousState.text,
                                usersTranslation = previousState.usersTranslation,
                                translatorSuggestion = "blabla bla"
                            )
                        } else if (previousState is AddCardState.SecondPageInput) {

                            FirstPageInput(
                                text = previousState.nativeText,
                                usersTranslation = previousState.usersTranslation
                            )
                        } else {
                            previousState
                        }
                    }
                }
            }

            AddCardCommand.SaveAndCreateNext -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is AddCardState.SecondPageInput) {
                        Log.d(
                            "debugowanko",
                            " state: $_state, state value:  ${_state.value}, próbuje zapisać notatke "
                        )
                        addCardUseCase(
                            0,
                            input = currentState.nativeText,
                            translation = currentState.usersTranslation.ifBlank { //jest zabezpieczone tutaj i jeszcze przez isEnough
                                currentState.translatorSuggestion ?: ""
                            }
                        )
                        Log.d(
                            "debugowanko",
                            " to z viewmodela po zrobieniu tak zwanego papa dla informacji "
                        )
                        _state.update { FirstPageInput("", "") }
                        Log.d(
                            "debugowanko",
                            " po daniu w viewmodel stateUpdate "
                        )
                    } else {
                        Log.d(
                            "debugowanko",
                            " state: $_state, state value:  ${_state.value}"
                        ) //do debugowania jak by trzeba było
                        addCardUseCase(0, "nojapier", "mtf")
                    } //przykładowy wpis jak coś nie wyjdzie
                }
            }

            AddCardCommand.SaveFiszka -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is AddCardState.SecondPageInput) {
                        try {
                            addCardUseCase(
                                0,
                                input = currentState.nativeText,
                                translation = currentState.usersTranslation.ifBlank { //jest zabezpieczone tutaj i jeszcze przez isEnough
                                    currentState.translatorSuggestion ?: ""
                                }
                            )
                            Log.d("debugowanko", "PO addCardUseCase – SUKCES!")
                        } catch (e: Exception) {
                            Log.e("debugowanko", "BŁĄD w addCardUseCase: ${e.message}", e)
                        }

                        _state.update { AddCardState.Finished }
                    } else {
                        Log.d(
                            "debugowanko",
                            " state: $_state, state value:  ${_state.value}"
                        ) //do debugowania jak by trzeba było
                        addCardUseCase(0, "nojapier", "mtf")
                    } //przykładowy wpis jak coś nie wyjdzie
                }
            }

            AddCardCommand.OnBackClick -> {
                closeTranslatorUseCase
                _state.update { AddCardState.Finished }

            }
        }
    }
}

sealed interface AddCardCommand {

    data class InputText(
        val nativeText: String,
        val usersTranslation: String,
        val isNative: Boolean
    ) : AddCardCommand

    data class AcceptSuggestion(val installed: Boolean) : AddCardCommand


    data object SaveFiszka : AddCardCommand

    data object SaveAndCreateNext : AddCardCommand

    data object OnCardClick : AddCardCommand

    data object OnBackClick : AddCardCommand
}


sealed interface AddCardState {


    data class FirstPageInput(
        val text: String = "",
        val usersTranslation: String = ""
    ) : AddCardState {
        val isEnough = text.isNotBlank() || usersTranslation.isNotBlank()
    }

    data class SecondPageInput(
        val nativeText: String,
        val usersTranslation: String = "",
        val translatorSuggestion: String?
    ) : AddCardState {
        val isEnough = usersTranslation.isNotBlank() && nativeText.isNotBlank()
    }

    data object Finished : AddCardState
}