package com.example.calendary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.controller.DayClickListener
import com.example.calendary.controller.adapter.MonthAdapter
import com.example.calendary.model.Day
import com.example.calendary.model.Month
import com.example.calendary.model.MonthEntry
import com.example.calendary.model.MonthWithDays
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
    }

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
