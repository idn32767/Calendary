package com.example.calendary.controller.adapter

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.model.Day
import com.example.calendary.model.Month
import com.example.calendary.controller.DayClickListener

class DayAdapter(private val days: List<Day>,
                 private val month : Month,
                 private val dayClickListener : DayClickListener) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val dayTxt: TextView = itemView.findViewById(R.id.day_txt)

        fun bind(day: Day,month : Month,dayClickListener: DayClickListener){
            dayTxt.text = "${day.number}"
            /* Назначение альтернативного фона, если на данный день назначено задание.
            *  Сейчас используются два идентичных изображения с разным цветом. Как сделать "параметризованное"
            *  изображение, параметры которого можно задать программно? */
            if((day.description != null) && (day.description != "")){
                dayTxt.setBackgroundResource(R.drawable.day_background_task)
            }
            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    dayClickListener.OnDayClick(month,day)
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days.get(position)
        holder.bind(day,month,dayClickListener)
    }
}
