package com.example.fiszki.data

import com.example.fiszki.domain.Fiszka


    fun Fiszka.toDbModel(): FiszkaDbModel{
        return FiszkaDbModel(id, input, translation, counter)
    }

    fun FiszkaDbModel.toEntity():Fiszka{
        return Fiszka(
            id = id,
            input = input,
            translation =  translation,
            counter =  counter)
    }
