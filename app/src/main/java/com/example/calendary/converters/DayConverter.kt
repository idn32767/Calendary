package com.example.calendary.converters

import androidx.room.TypeConverter
import java.util.UUID

/**
 * Класс для преобразования наших типов в типы понятные Sqlite и наоборот
 */
class DayConverter {



    @TypeConverter
    fun toUUID(uuid: String): UUID?{
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID):String? {
        return uuid?.toString()
    }
}