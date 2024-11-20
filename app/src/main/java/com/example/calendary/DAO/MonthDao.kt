package com.example.calendary.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.calendary.model.Day
import com.example.calendary.model.Month
import com.example.calendary.model.MonthWithDays

@Dao
interface MonthDao {
    @Query("SELECT * FROM month")
    suspend fun getMonths(): List<Month>

    @Query("SELECT * FROM month")
    suspend fun getMonthsWithDays(): List<MonthWithDays>

    @Query("SELECT * FROM month WHERE month.monthId=:mId LIMIT 1")
    suspend fun getMonthById(mId : Int) : Month

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(month: Month)
}
