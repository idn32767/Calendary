package com.example.calendary.model

class Month {
    var title: String = ""
    lateinit var days: ArrayList<Day>

    constructor(title: String, days: ArrayList<Day>){
        this.title = title
        this.days = days
    }
}