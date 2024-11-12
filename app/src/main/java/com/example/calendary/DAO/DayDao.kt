package com.example.calendary.DAO

import android.media.audiofx.AudioEffect.Descriptor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.calendary.model.Day

@Dao
interface DayDao {
    @Query("SELECT * FROM day")
    fun getDays(): List<Day>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDay(day: Day)

}