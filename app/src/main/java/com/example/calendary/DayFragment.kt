package com.example.calendary
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.util.Log
import com.example.calendary.R

class DayFragment : Fragment(R.layout.day_edit_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(container == null){
            Log.d("DayFragment","no container selected")
        }
        val fragmentView = inflater.inflate(R.layout.day_edit_fragment,container,false)
        Log.d("DayFragment","initializing view")
        if (fragmentView == null)
        {
            Log.d("DayFragment","cannot create view")
        }
        return fragmentView
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}
