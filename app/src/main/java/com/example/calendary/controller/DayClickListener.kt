package com.example.calendary.controller
import com.example.calendary.model.Day
import com.example.calendary.model.Month

interface DayClickListener {
    fun OnDayClick(month : Month,day : Day)
}
