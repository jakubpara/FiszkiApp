package com.example.fiszki.domain.usecase

import com.example.fiszki.data.TranslatorDataSource
import javax.inject.Inject

class CloseTranslatorUseCase @Inject constructor(
    private val translatorDataSource: TranslatorDataSource
) {
     operator fun invoke (){
        translatorDataSource.close()
    }
}