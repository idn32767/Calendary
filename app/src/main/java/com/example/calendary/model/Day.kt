package com.example.calendary.model

class Day {
    var number: Int = 0
    var description: String = ""

    constructor(number: Int){
        this.number = number
    }

    constructor(number: Int, description: String){
        this.number = number;
        this.description = description
    }
}