package com.example.lab_week_10.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Total::class], version = 1)
abstract class TotalDatabase : RoomDatabase() {

    abstract fun totalDao(): TotalDao

    companion object {
        @Volatile
        private var INSTANCE: TotalDatabase? = null

        fun getDatabase(context: Context): TotalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TotalDatabase::class.java,
                    "total_database"
                )
                    .allowMainThreadQueries() // For simplicity, not for production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}