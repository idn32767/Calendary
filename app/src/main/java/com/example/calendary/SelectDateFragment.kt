package com.example.calendary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import com.example.calendary.R
import com.example.calendary.controller.DayClickListener
import com.example.calendary.controller.adapter.MonthAdapter
import com.example.calendary.model.Day
import com.example.calendary.model.Month
import com.example.calendary.model.MonthEntry
import com.example.calendary.model.MonthWithDays
import com.example.calendary.ApplicationRepository
import java.time.LocalDate
import java.util.UUID

class SelectDateFragment : Fragment(),DayClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SelectDateFragment","onCreate")
    }

    /* FIXME: почему это нужно указывать здесь? */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SelectDateFragment","onCreateView")
        val fragmentView = inflater.inflate(R.layout.select_date_fragment,container,false)
        initializeFragment(fragmentView)
        return fragmentView
    }

    /* FIXME: почему это нужно указывать здесь? */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeFragment(view : View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.month_list)

        /* FIXME: пока так. потом можно будет заменить реализацию */
        //monthAdapter = MonthAdapter(monthList,this);
        //recyclerView.adapter = monthAdapter; // Назначаем адаптер списку месяцев
        recyclerView.layoutManager = LinearLayoutManager(activity) // Указываем менеджер компоновки для списка месяцев

        val currentDate = LocalDate.now()
        val monthList: ArrayList<MonthWithDays> = ArrayList<MonthWithDays>()
        val entries = listOf(
            MonthEntry(1,java.time.Month.JANUARY,"Январь"),
            MonthEntry(2,java.time.Month.FEBRUARY,"Февраль"),
            MonthEntry(3,java.time.Month.FEBRUARY,"Март"),
            MonthEntry(4,java.time.Month.FEBRUARY,"Апрель")
        )

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

        val monthAdapter = MonthAdapter(monthList,this);
        recyclerView.adapter = monthAdapter; // Назначаем адаптер списку месяцев
        recyclerView.layoutManager = LinearLayoutManager(activity) // Указываем менеджер компоновки для списка месяцев
        val database = (activity?.application as MyApplication).database

        /* FIXME: вряд ли такое количество данных стоит передавать через событие,
        *         поэтому получаем их здесь из базы. Какой объект по правилам
        *         должен этим заниматься? */
        viewLifecycleOwner.lifecycleScope.launch {
            val months = database.monthDao().getMonthsWithDays()
            val modifiedMonthIds: MutableSet<Int> = mutableSetOf()
            for ((dbMonthId, dbMonth) in months.withIndex()) {
                Log.d("SelectDateFragment","month: ${dbMonth.month.id} / ${dbMonth.month.monthId}")
                /* Сыграем на том, что данные для отображения расположены по порядку */
                val viewMonth = monthList[dbMonth.month.monthId - 1]
                var hasModifiedDates = false
                for (dayIdx in 0..<viewMonth.days.size) {
                    val foundDay = dbMonth.days.find { it.number == viewMonth.days[dayIdx].number }
                    if (foundDay != null) {
                        Log.d("SelectDateFragment","Дата : ${dbMonth.month.title} / ${foundDay.number} / ${foundDay.description}")
                        viewMonth.days[dayIdx] = foundDay
                        hasModifiedDates = true
                    }
                }
                if(hasModifiedDates){
                    Log.d("SelectDateFragment","Добавление изменений в месяц: ${dbMonth.month.monthId}")
                    modifiedMonthIds.add(dbMonth.month.monthId - 1)
                }
            }
            launch(Dispatchers.Main) {
                /* После получения данных отправляем всё в основной поток и обновляем интерфейс */
                for (modifiedMonthId in modifiedMonthIds) {
                    Log.d("notify", modifiedMonthId.toString())
                    monthAdapter.notifyItemChanged(modifiedMonthId)
                }
            }
        }



    }

    /* Обработчик нажатия на конкретный день месяца. Передаём данные в основное приложение
    *  через событие. Это один из основных способов передачи данных в том случае, когда их не так много
    *  и нужно передать их однократно. */
    override fun OnDayClick(month : Month,day: Day) {
        Log.d("SelectDataFragment","Clicked on day ${month.title} / ${day.number}")
        val msgBundle = Bundle()
        msgBundle.putInt("monthNumber",month.monthId)
        msgBundle.putString("monthUUID",month.id.toString())
        msgBundle.putString("monthTitle",month.title)
        msgBundle.putString("dayId",day.id.toString())
        msgBundle.putInt("dayNumber",day.number)
        msgBundle.putString("description",day.description)
        ApplicationRepository.fragmentManager.setFragmentResult("dateSelected",msgBundle)
    }
}
