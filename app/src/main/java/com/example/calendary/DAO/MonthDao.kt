package com.example.calendary.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.calendary.model.Month

@Dao
interface MonthDao {
    @Query("SELECT * FROM month")
    fun getMonths(): List<Month>
}