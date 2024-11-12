package com.example.calendary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.calendary.DAO.DayDao
import com.example.calendary.DAO.MonthDao
import com.example.calendary.converters.DayConverter
import com.example.calendary.converters.MonthConverter

import com.example.calendary.model.Day
import com.example.calendary.model.Month


@Database(entities = [Day::class, Month::class], version = 1)
//@TypeConverters(DayConverter::class, MonthConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dayDao():DayDao
    abstract fun monthDao():MonthDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calendar-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}