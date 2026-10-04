package com.example.fiszki.presentation.screens.ReviewScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.usecase.DeleteCardUseCase
import com.example.fiszki.domain.usecase.DontRememberUseCase
import com.example.fiszki.domain.usecase.GetNextExampleUseCase
import com.example.fiszki.domain.usecase.IKnowItUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    private val dontRememberUseCase: DontRememberUseCase,
    private val iKnowItUseCase: IKnowItUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val getNextExampleUseCase: GetNextExampleUseCase

) : ViewModel() {


    private val _state = MutableStateFlow<ReviewingState>(ReviewingState.Initial)
    val state = _state.asStateFlow()

    init {//TODO(Trzeba zrobić obsługe wyjątku że jest pusta lista i żeby nie wywalało zrbić rózne usecasy suspend)
        viewModelScope.launch {


                val nextExample = getNextExampleUseCase()
                if (nextExample != null) {
                    _state.value = ReviewingState.SeeFirstPage(nextExample)
                } else {
                    _state.value = ReviewingState.EmptyListOrRecordsOver
                }
        }
    }


    fun processCommand(command: ReviewingCommand) {
        when (command) {
            is ReviewingCommand.DontRememberClick -> {
                    viewModelScope.launch {
                        if (_state.value is ReviewingState.SeeFirstPage) {
                            val fiszka = (_state.value as ReviewingState.SeeFirstPage).fiszka
                            dontRememberUseCase(id = fiszka.id)
                            val nextExample = getNextExampleUseCase()
                            _state.value = if (nextExample != null) {
                                ReviewingState.SeeFirstPage(nextExample)
                            } else {
                                ReviewingState.EmptyListOrRecordsOver
                            }
                        }
                    }
            }

            is ReviewingCommand.IKnowItClick -> {
                viewModelScope.launch {
                    if (_state.value is ReviewingState.SeeFirstPage){
                        val fiszka = (_state.value as ReviewingState.SeeFirstPage).fiszka
                        iKnowItUseCase(fiszka.id)
                        val nextExample = getNextExampleUseCase()
                        _state.value = if (nextExample != null) {
                            ReviewingState.SeeFirstPage(nextExample)
                        } else {
                            ReviewingState.EmptyListOrRecordsOver
                        }
                    }
                }

            }

            is ReviewingCommand.DeleteCardClick -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is ReviewingState.SeeFirstPage) {
                        val fiszka = currentState.fiszka
                        deleteCardUseCase(fiszka.id)
                        val nextExample = getNextExampleUseCase()
                        _state.value = if (nextExample != null) {
                            ReviewingState.SeeFirstPage(nextExample)
                        } else {
                            ReviewingState.EmptyListOrRecordsOver
                        }
                    }
                }
            }
        }
    }
}

sealed interface ReviewingCommand {

    data object DontRememberClick : ReviewingCommand

    data object IKnowItClick : ReviewingCommand

    data object DeleteCardClick : ReviewingCommand

}


sealed interface ReviewingState {
    data object Initial : ReviewingState

    data class SeeFirstPage(val fiszka: Fiszka) : ReviewingState

    data object EmptyListOrRecordsOver : ReviewingState

    data object Finished : ReviewingState
}