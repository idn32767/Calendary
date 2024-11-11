package com.example.calendary.controller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.model.Month


/**
 * @class - адаптер для списка месяцев
 * @data - 11.11.2024
 * @author - Vlad
 * Класс для заполнения списка месяцев
 */
class MonthAdapter(private val months: List<Month>): RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {


    /**
     * Внутренний класс - служит для связи с разметкой в каталоге layout
     */
    inner class MonthViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val monthTxt: TextView = itemView.findViewById(R.id.month_txt)           // Првязываемся к элементу файла который привязали
        val recyclerView: RecyclerView =itemView.findViewById(R.id.days_list)
        fun bind(month: Month){                                                  // Метод, который выводит в конкретный элемент списка, значения конктреного объекта модели
            monthTxt.text = "${month.title}"
            val dayAdapter: DayAdapter
            dayAdapter = DayAdapter(month.days);
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
            recyclerView.adapter = dayAdapter;



        }
    }

    /**
     * Метод, который создает элемент из файла разметки и привязывает его к внутренниму классу
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.month_item,
            parent, false);
        return MonthViewHolder(view)
    }

    /**
     * Метод, который возвращает количетсво элементов в списке объектов моделей
     */
    override fun getItemCount(): Int {
       return months.size
    }

    /**
     * Метод, который принимает объект внутренего класса
     * и номер элемента, который идет по счету в списке
     * Данный метод связывает номер элемента и его отображение на экране
     */
    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
       val month = months.get(position)
        holder.bind(month)
    }
}