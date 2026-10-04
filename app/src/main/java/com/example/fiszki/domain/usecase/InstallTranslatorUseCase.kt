package com.example.fiszki.domain.usecase

import android.util.Log
import com.example.fiszki.data.TranslatorDataSource
import javax.inject.Inject

class InstallTranslatorUseCase @Inject constructor(
        private val translatorDataSource: TranslatorDataSource
    ) {
        suspend operator fun invoke (){
            Log.d("Test translatora", " use case przekazuje zainstaluj")
            translatorDataSource.downloadTranslation()
        }
    }
