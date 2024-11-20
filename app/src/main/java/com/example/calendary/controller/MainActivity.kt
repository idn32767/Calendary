package com.example.calendary.controller

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.calendary.AppDatabase
import com.example.calendary.DAO.DayDao
import com.example.calendary.MyApplication
import com.example.calendary.R
import com.example.calendary.model.Month
import  com.example.calendary.model.MonthWithDays
import com.example.calendary.controller.adapter.MonthAdapter
import com.example.calendary.model.Day
import kotlin.collections.MutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.calendary.DayFragment

/* FIXME: почему это нужно указывать здесь? */
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity(),DayClickListener  {
    lateinit var recyclerView: RecyclerView  //Переменная, которая связывается с разметкой - список

    // Получаем экземпляр для работы с базой данных из класса MyApplication ленивой инициализацией
    private val database by lazy { (application as MyApplication).database }
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    /**
     * Метод, который вызывается при создании Активти
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)                    // Привязываем разметку к классу
        recyclerView = findViewById(R.id.month_list)              // Получаем связь со списком из разметки

        val appDatabase: AppDatabase = AppDatabase.getDatabase(this)
        val currentDate = LocalDate.now()
        val monthList: ArrayList<MonthWithDays> = ArrayList<MonthWithDays>()
        val entries = listOf(MonthEntry(1,java.time.Month.JANUARY,"Январь"),
                             MonthEntry(2,java.time.Month.FEBRUARY,"Февраль"),
                             MonthEntry(3,java.time.Month.FEBRUARY,"Март"),
                             MonthEntry(4,java.time.Month.FEBRUARY,"Апрель"))
        for((entryId,entry) in entries.withIndex())
        {
            val realMonth = java.time.YearMonth.of(currentDate.year,entry.month)
            val dayList = ArrayList<Day>()
            for(dayId in 1..realMonth.lengthOfMonth())
            {
                dayList.add(Day(UUID.randomUUID(),dayId,"", UUID.randomUUID()))
            }
            monthList.add(MonthWithDays(Month(UUID.randomUUID(),entryId + 1,entry.monthName),dayList))
        }

        val dayDao = appDatabase.dayDao()
        /*val newDay = Day(
            id = UUID.randomUUID(),  // Уникальный идентификатор
            number = 5,  // Пример: 5-е число месяца
            description = "Важное событие",
        )*/

        // Создаем адаптер, для списка месяцев
        val monthAdapter: MonthAdapter
        /* FIXME: пока так. потом можно будет заменить реализацию */
        monthAdapter = MonthAdapter(monthList,this);
        recyclerView.adapter = monthAdapter; // Назначаем адаптер списку месяцев
        recyclerView.layoutManager = LinearLayoutManager(this) // Указываем менеджер компоновки для списка месяцев

        lifecycleScope.launch{
            var months = appDatabase.monthDao().getMonthsWithDays()
            val modifiedMonthIds : MutableSet<Int> = mutableSetOf()
            for((dbMonthId,dbMonth) in months.withIndex()){
                /* Сыграем на том, что данные для отображения расположены по порядку */
                val viewMonth = monthList[dbMonth.month.monthId - 1]
                for(dayIdx in 0..<viewMonth.days.size) {
                    val foundDay = dbMonth.days.find { it.number == viewMonth.days[dayIdx].number }
                    if(foundDay != null) {
                        viewMonth.days[dayIdx] = foundDay
                        modifiedMonthIds.add(dbMonthId)
                    }
                }
            }
            launch (Dispatchers.Main){
                for(modifiedMonthId in modifiedMonthIds){
                    Log.d("notify",modifiedMonthId.toString())
                    monthAdapter.notifyItemChanged(modifiedMonthId)
                }
            }
        }

// Запустите вставку в базу данных в корутине


        // Запускаем AsyncTask для вставки в базу данных
        //InsertDayTask(dayDao).execute(newDay)


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


    }

    override fun OnDayClick(day: Day) {
        Log.d("MainActivity","Нажали на ${day.monthId} / ${day.number}")
        val dayFragment = DayFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_view,dayFragment)
            .setReorderingAllowed(true)
            .commit()
    }


    private data class MonthEntry(val id : Int,val month : java.time.Month,val monthName : String)

    /**
     * Проверяет, является ли сочетание числа, месяца и года корректной датой
     * */
    private fun dateIsValid(year : Int,month : Int, day : Int) : Boolean{
        val formatted = String.format(Locale.ROOT,"%04d-%02d-%02d",year,month,day)
        var success = false
        try {
            val dt = LocalDate.parse(formatted,dateFormatter)
            success = true
        }
        catch (ex : Exception) {
            success = false
        }
        return success
    }
    /**
     * Класс для запуска задачи в фоновом потоке
     */
    //private class InsertDayTask(private val dayDao: DayDao) : AsyncTask<Day, Unit, Unit>() {
        /**
         * Фоновая работа
         */
     /*   @SuppressLint("WrongThread")
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
    }*/

    //private class SelectDayTask(private val dayDao: DayDao) : AsyncTask<Unit, Unit, Unit>() {
        /**
         * Фоновая работа
         */
       /* override fun doInBackground(vararg p0: Unit?) {
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
    }*/
}