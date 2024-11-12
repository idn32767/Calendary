package com.example.calendary.model

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Day(
    @PrimaryKey  val id: UUID = UUID.randomUUID(),
    val number: Int,
    val description: String = "",
  //  @ColumnInfo(index = true) val monthId: UUID  // Внешний ключ для связи с таблицей "months"

)