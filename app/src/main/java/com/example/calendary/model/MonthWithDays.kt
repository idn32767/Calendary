package com.example.calendary.model

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Модель отношения между месяцем и днём
 * позволяет получить месяц со всеми днями
 * */
data class MonthWithDays(
    @Embedded val month : Month,
    @Relation(
        parentColumn = "id",
        entityColumn = "monthId"
    )
    val days: MutableList<Day>
)
