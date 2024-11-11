package com.example.calendary.controller

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.controller.adapter.MonthAdapter
import com.example.calendary.model.Day
import com.example.calendary.model.Month


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView  //Переменная, которая связывается с разметкой - список

    /**
     * Метод, который вызывается при создании Активти
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)                    // Привязываем разметку к классу
        recyclerView = findViewById(R.id.month_list)              // Получаем связь со списком из разметки




        Log.d("Этап цикла: ", " onCreate")
        // Собираем данные для списков
        val monthList: ArrayList<Month>
        monthList = ArrayList()
        val  daysList: ArrayList<Day>
        daysList = ArrayList()
        for (n in 1..30) {
            daysList.add(Day(n))
        }

        monthList.add(Month("Январь", daysList))
        monthList.add(Month("Февраль", daysList))
        monthList.add(Month("Март", daysList))
        monthList.add(Month("Апрель", daysList))

        // Создаем адаптер, для списка месяцев
        val monthAdapter: MonthAdapter
        monthAdapter = MonthAdapter(monthList);
        recyclerView.adapter = monthAdapter; // Назначаем адаптер списку месяцев
        recyclerView.layoutManager = LinearLayoutManager(this) // Указываем менеджер компоновки для списка месяцев
    }

    override fun onStart() {
        super.onStart()
        Log.d("Этап цикла: ", " onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Этап цикла: ", " onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Этап цикла: ", " onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Этап цикла: ", " onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Этап цикла: ", " onDestroy")
    }
}