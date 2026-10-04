package com.example.fiszki.di

import android.content.Context
import com.example.fiszki.data.FiszkiDao
import com.example.fiszki.data.FiszkiDatabase
import com.example.fiszki.data.FiszkiRepositoryImpl
import com.example.fiszki.data.SettingsRepositoryImpl
import com.example.fiszki.domain.repository.FiszkiRepository
import com.example.fiszki.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {


    @Binds
    @Singleton
    fun bindFiszkiRepository(
        impl: FiszkiRepositoryImpl
    ): FiszkiRepository

    @Binds
    @Singleton
    fun bindSettings(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): FiszkiDatabase {
            return FiszkiDatabase.getInstance(context)
        }


        @Singleton
        @Provides
        fun provideNotesDao(
            database: FiszkiDatabase
        ): FiszkiDao {
            return database.fiszkiDao()

        }
    }
}