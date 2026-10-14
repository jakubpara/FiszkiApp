package com.example.fiszki.presentation.screens.PracticeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.repository.SettingsRepository
import com.example.fiszki.domain.usecase.DeleteCardUseCase
import com.example.fiszki.domain.usecase.DontRememberUseCase
import com.example.fiszki.domain.usecase.GetAllCardsUseCase
import com.example.fiszki.domain.usecase.GetNextExampleUseCase
import com.example.fiszki.domain.usecase.IKnowItUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeScreenViewModel @Inject constructor(
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val getNextExampleUseCase: GetNextExampleUseCase,
    private val iKnowItUseCase: IKnowItUseCase,
    private val dontRememberUseCase: DontRememberUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PracticeState>(PracticeState.Initial)
    val state = _state.asStateFlow()

    private fun PracticeStateData(
        fiszka: Fiszka,
        allCards: List<Fiszka>,
        nativeLanguage: Boolean
    ): PracticeState.Practicing {

        val question = if (nativeLanguage) fiszka.input else fiszka.translation

        val correctAnswer = if (nativeLanguage) fiszka.translation else fiszka.input

        val wrongAnswers = allCards
            .map { if (nativeLanguage) it.translation else it.input }
            .filter { it != correctAnswer }
            .shuffled()
            .distinct()
            .take(3)

        val others= listOf("brak", "brak", "brak")// tu miożna nawymyślać słówek ale i tak będą sie przewijać tylko te z db
        val allWrong = (wrongAnswers + others).take(3)
        val allAnswers = (listOf(correctAnswer) + allWrong).shuffled()

        return PracticeState.Practicing(
            fiszka = fiszka,
            question = question,
            correctAnswer = correctAnswer,
            answers = allAnswers,
            correctIndex = allAnswers.indexOf(correctAnswer),
            nativeLanguage = nativeLanguage
        )
    }
    private suspend fun loadNextCard() {
        val allCards = getAllCardsUseCase()
        val settings = settingsRepository.getSettings().first()
        val nextExample = getNextExampleUseCase()

        _state.value = if (nextExample != null) {
            PracticeStateData(nextExample, allCards, settings.nativeLanguage)
        } else {
            PracticeState.EmptyListOrRecordsOver
        }
    }


    init {
        viewModelScope.launch {
            loadNextCard()
        }
    }





    fun processCommand(command: PracticeCommand) {
        when (command) {
            is PracticeCommand.OnAnswerClick -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is PracticeState.Practicing) {
                        val fiszka = currentState.fiszka ?: return@launch // xdd sie okazuje że takie coś dajesz i mówisz którą funkcje ma
                        // zamknąć bo nie całość tylko tą po kropce lauch sie okazuje że możesz też dać return@forEach return@map
                        // return@let i to konczy to co jest po @ a nie całą funkcje

                        if (command.correct) {
                            iKnowItUseCase(fiszka.id)
                        } else {
                            dontRememberUseCase(fiszka.id)
                        }

                        loadNextCard()
                    }
                }
            }

            PracticeCommand.DeleteCardClick -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is PracticeState.Practicing) {
                        currentState.fiszka?.let { deleteCardUseCase(it.id) }
                        loadNextCard()
                    }
                }
            }
        }
    }
}

sealed interface PracticeCommand {

    data class OnAnswerClick(val option: Int, val correct: Boolean) : PracticeCommand

    data object DeleteCardClick : PracticeCommand
}

sealed interface PracticeState {

    data object Initial : PracticeState

    data class Practicing(
        val fiszka: Fiszka?,
        val question: String,
        val correctAnswer: String,
        val answers: List<String>,
        val correctIndex: Int,
        val nativeLanguage: Boolean
    ) : PracticeState

    data object EmptyListOrRecordsOver : PracticeState
}