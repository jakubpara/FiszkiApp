package com.example.fiszki.domain.usecase

import com.example.fiszki.data.TranslatorDataSource
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
   private val translatorDataSource: TranslatorDataSource
) {
     suspend operator fun invoke ( text: String): String{
        return translatorDataSource.translate(text)
    }
}