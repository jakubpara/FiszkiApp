package com.example.fiszki.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fiszka")
class FiszkaDbModel (
@PrimaryKey(autoGenerate = true)
val id: Int,
val input: String,
val translation: String,
val counter: Int
)