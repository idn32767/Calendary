package com.example.calendary.converters

import androidx.room.TypeConverter
import java.util.UUID

class MonthConverter {

    @TypeConverter
    fun toUUID(uuid: String): UUID?{
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID):String? {
        return uuid?.toString()
    }
}