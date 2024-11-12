package com.example.calendary.controller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.model.Day

class DayAdapter(private val days: List<Day>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val dayTxt: TextView = itemView.findViewById(R.id.day_txt)

        fun bind(day: Day){
            dayTxt.text = "${day.number}"
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
        holder.bind(day)
    }
}