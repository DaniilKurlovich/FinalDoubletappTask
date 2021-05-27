package com.example.finaltaskandroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PracticeEntity::class], version = 1)
abstract class PracticeDatabase : RoomDatabase() {
    abstract fun practiceDao(): PracticeDao

    companion object {
        private var INSTANCE: PracticeDatabase? = null
        fun getInstance(context: Context): PracticeDatabase? {
            if (INSTANCE == null) {
                synchronized(PracticeDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PracticeDatabase::class.java,
                            "practice_database"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }

}