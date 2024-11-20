package com.example.calendary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "month")  // Указываем имя таблицы
data class Month(
    @PrimaryKey val id: UUID = UUID.randomUUID(),  // Уникальный идентификатор
    val monthId : Int,
    val title: String  // Название месяца
)