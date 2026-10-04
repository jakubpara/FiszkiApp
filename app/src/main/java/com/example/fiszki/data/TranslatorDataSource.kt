package com.example.fiszki.data

import android.content.Context
import android.util.Log
import com.example.fiszki.domain.repository.SettingsRepository
import com.example.fiszki.domain.usecase.UpdateInstallationStatusUseCase
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TranslatorDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val updateInstallationStatusUseCase: UpdateInstallationStatusUseCase
) {

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.POLISH)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    val polishEnglishTranslator = Translation.getClient(options)

    suspend fun downloadTranslation() {
        Log.d("Test translatora", " doszło do data layer")
        coroutineScope {
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            polishEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    Log.d("Test translatora", " sukces")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateInstallationStatusUseCase(true)
                    }
                }
                .addOnFailureListener {
                    Log.d("Test translatora", " blad")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateInstallationStatusUseCase(false)
                    }
                }
        }
    }

     suspend fun translate(text: String): String =
         polishEnglishTranslator.translate(text).await().toString() // z tym await mi chat podpowiedział bo mi ciagle zwracało kotlin.Unit


    fun close() {
        polishEnglishTranslator.close()
    }
}




