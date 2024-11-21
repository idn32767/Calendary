package com.example.calendary.controller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendary.R
import com.example.calendary.model.Month
import com.example.calendary.model.MonthWithDays
import com.example.calendary.controller.DayClickListener
import com.example.calendary.controller.adapter.MonthAdapter.MonthViewHolder
import com.example.calendary.model.Day

class SelectedDateAdapter  {
    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateHeaderView : TextView = itemView.findViewById(R.id.txt_date)
        val eventEdit : EditText = itemView.findViewById(R.id.edit_event)
        val btnApply : Button = itemView.findViewById(R.id.btn_apply)
        fun bind(day : Day) {
        }
    }


}
