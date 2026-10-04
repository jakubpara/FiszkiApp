package com.example.fiszki.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(FiszkaDbModel::class),
    version = 1,
    exportSchema = false
)
abstract class FiszkiDatabase: RoomDatabase() {

    abstract fun fiszkiDao(): FiszkiDao

    companion object{

        private var instance: FiszkiDatabase? = null
        private  val LOCK = Any()

        fun getInstance(context: Context): FiszkiDatabase{

            instance?.let { return it }

            synchronized(LOCK){
                instance?.let { return it }

                return Room.databaseBuilder(
                    context = context,
                    klass = FiszkiDatabase::class.java,
                    name = "fiszka.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build().also {
                    instance = it
                }
            }
        }
    }
}