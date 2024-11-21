package com.example.calendary
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
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
        val dateHeaderView : TextView = fragmentView.findViewById(R.id.txt_date)
        val eventEdit : EditText = fragmentView.findViewById(R.id.edit_event)
        val btnApply : Button = fragmentView.findViewById(R.id.btn_apply)

        Log.d("DayFragment","initializing view")
        if (fragmentView == null)
        {
            Log.d("DayFragment","cannot create view")
        }

        btnApply.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?){
                Log.d("DayFragment","Apply was clicked")
                //val args = bundleOf()
                //ApplicationRepository.fragmentManager.setFragmentResult()
            }
        })

        return fragmentView
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}
