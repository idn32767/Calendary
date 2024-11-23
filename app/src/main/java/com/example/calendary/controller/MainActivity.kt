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
import com.example.calendary.ApplicationRepository
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.calendary.DayFragment
import com.example.calendary.SelectDateFragment
import com.example.calendary.model.MonthEntry

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

        ApplicationRepository.fragmentManager = supportFragmentManager
        if(!ApplicationRepository.hasFragment()){
            ApplicationRepository.currentFragment = SelectDateFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_layout,ApplicationRepository.currentFragment)
                .setReorderingAllowed(true)
                .commit()
        }

        /* Подписываемся на сообщения от фрагментов */
        supportFragmentManager.setFragmentResultListener("dateSelected",this,{
              requestKey,bundle -> OnDateSelected(requestKey,bundle)
        })
        supportFragmentManager.setFragmentResultListener("dateModified",this,{
                requestKey,bundle -> OnDateModified(requestKey,bundle)
        })

        Log.d("Этап цикла: ", " onCreate")

    }

    override fun OnDayClick(month : Month,day: Day) {
        /*Log.d("MainActivity","Нажали на ${day.monthId} / ${day.number}")
        val dayFragment = DayFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_view,dayFragment)
            .setReorderingAllowed(true)
            .commit()*/
    }

    private fun OnDateSelected(requestKey : String,bundle : Bundle?){
        val dayFragment = DayFragment()
        dayFragment.arguments = bundle
        ApplicationRepository.currentFragment = dayFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_layout,dayFragment)
            .setReorderingAllowed(true)
            .commit()
    }

    /**
     * Запускается по событию, в котором в качестве параметров указываются дата
     * и введённое пользоватеклем событие. Запускает вставку дня и месяца,
     * ф затем в главном потоке снова активирует фрагмент для выбора дат
     * */
    private fun OnDateModified(requestKey : String,bundle : Bundle?){
        /* Запуск асинхронной операции в предназначенном для этого контексте */
        lifecycleScope.launch {
            if(bundle != null){
                modifyDayByBundle(bundle)
                /* Практически у любого UI любые манипуляции с ним
                *  должны производиться в основном потоке*/
                launch (Dispatchers.Main){
                    val selectDateFragment = SelectDateFragment()
                    ApplicationRepository.currentFragment = selectDateFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout,selectDateFragment)
                        .setReorderingAllowed(true)
                        .commit()
                }
            }
        }
    }

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
     * Записывает изменённые данные, которые находятся в событии,
     * в базу
     * */
    private suspend fun modifyDayByBundle(bundle : Bundle){
        val monthUUID = UUID.fromString(bundle.getString("monthUUID"))
        val monthNumber = bundle.getInt("monthNumber")
        val monthTitle = bundle.getString("monthTitle") ?: ""
        val dayId = UUID.fromString(bundle.getString("dayId"))
        val dayNumber = bundle.getInt("dayNumber")
        val description = bundle.getString("description") ?: ""
        /* FIXME: назначить транзакцию ? */
        val resMonthId = addOrModifyMonth(monthUUID,monthNumber,monthTitle)
        val resDayId = addOrModifyDay(dayId,monthUUID,dayNumber,description)
    }

    /* Добавление или изменение месяца в БД */
    private suspend fun addOrModifyMonth(monthId : UUID,monthNumber : Int,monthTitle : String){
        val month = Month(monthId,monthNumber,monthTitle)
        val resMonthId = database.monthDao().insertMonth(month)
        Log.d("MainActivity","New Month : ${monthId} / ${month.id} / ${month.title}")
        return resMonthId
    }

    /* Добавление или изменение события в БД */
    private suspend fun addOrModifyDay(dayId : UUID,monthId : UUID,dayNumber : Int,description : String){
        val day = Day(dayId,dayNumber,description,monthId)
        var resDayId = database.dayDao().insertDay(day)
        Log.d("MainActivity","New Day : ${monthId} / ${dayId} / ${day.id} / ${description}")
        return resDayId
    }


}