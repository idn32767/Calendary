package com.example.calendary.controller

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.AppDatabase
import com.example.calendary.DAO.DayDao
import com.example.calendary.MyApplication
import com.example.calendary.R
import com.example.calendary.controller.adapter.MonthAdapter
import com.example.calendary.model.Day
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.util.UUID


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView  //Переменная, которая связывается с разметкой - список

    // Получаем экземпляр для работы с базой данных из класса MyApplication ленивой инициализацией
    private val database by lazy { (application as MyApplication).database }
    /**
     * Метод, который вызывается при создании Активти
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)                    // Привязываем разметку к классу
        recyclerView = findViewById(R.id.month_list)              // Получаем связь со списком из разметки


        var appDatabase: AppDatabase = AppDatabase.getDatabase(this)
        val dayDao = appDatabase.dayDao()
        val newDay = Day(
            id = UUID.randomUUID(),  // Уникальный идентификатор
            number = 5,  // Пример: 5-е число месяца
            description = "Важное событие",
        )

// Запустите вставку в базу данных в корутине


        // Запускаем AsyncTask для вставки в базу данных
        InsertDayTask(dayDao).execute(newDay)


    //    }
        // Пример использования базы данных
//        lifecycleScope.launch {
//            val month = Month(title = "January")
//            val monthId = database.monthDao().insertMonth(month) // Теперь monthId содержит ID записи
//            println("Inserted month ID: $monthId")
//
//            val day = Day(number = 1, description = "New Year's Day", monthId = monthId.toInt())
//            database.dayDao().insertDay(day)
//
//            val daysForMonth = database.dayDao().getDaysForMonth(monthId.toInt())
//            println("Days in January: $daysForMonth")
//        }


        Log.d("Этап цикла: ", " onCreate")
        // Собираем данные для списков
//        val monthList: ArrayList<Month>
//        monthList = ArrayList()
//        val  daysList: ArrayList<Day>
//        daysList = ArrayList()
//        for (n in 1..30) {
//            daysList.add(Day(n))
//        }
//
//        monthList.add(Month("Январь", daysList))
//        monthList.add(Month("Февраль", daysList))
//        monthList.add(Month("Март", daysList))
//        monthList.add(Month("Апрель", daysList))

        // Создаем адаптер, для списка месяцев
        val monthAdapter: MonthAdapter
 //       monthAdapter = MonthAdapter(monthList);
      //  recyclerView.adapter = monthAdapter; // Назначаем адаптер списку месяцев
        recyclerView.layoutManager = LinearLayoutManager(this) // Указываем менеджер компоновки для списка месяцев
    }

    /**
     * Класс для запуска задачи в фоновом потоке
     */
    private class InsertDayTask(private val dayDao: DayDao) : AsyncTask<Day, Unit, Unit>() {
        /**
         * Фоновая работа
         */
        @SuppressLint("WrongThread")
        override fun doInBackground(vararg days: Day?) {
            days.forEach { day ->
                if (day != null) {
                    dayDao.insertDay(day)  // Выполняем вставку
                }
            }
            Log.d("Начали ", "Получение")

            SelectDayTask(dayDao).execute(Unit)

        }
        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            println("День успешно добавлен в базу данных")
        }
    }

    private class SelectDayTask(private val dayDao: DayDao) : AsyncTask<Unit, Unit, Unit>() {
        /**
         * Фоновая работа
         */
        override fun doInBackground(vararg p0: Unit?) {
          Log.d("Получение ", dayDao.getDays().toString())
        }
        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            println("День успешно получени из базы данных")
        }

        override fun onPreExecute() {
            super.onPreExecute()
            println("День ")
        }
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