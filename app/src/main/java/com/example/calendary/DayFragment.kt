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
import java.util.UUID

class DayFragment : Fragment(R.layout.day_edit_fragment) {

    private var _monthUUID = UUID.randomUUID()
    private var _monthNumber = 0
    private var _monthTitle = ""
    private var _dayId = UUID.randomUUID()
    private var _dayNumber = 0
    private var _description = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentArguments = arguments
        if(currentArguments != null){
            _monthUUID = UUID.fromString(currentArguments.getString("monthUUID"))
            _monthNumber = currentArguments.getInt("monthNumber")
            _monthTitle = currentArguments.getString("monthTitle") ?: ""
            _dayId = UUID.fromString(currentArguments.getString("dayId"))
            _dayNumber = currentArguments.getInt("dayNumber")
            _description = currentArguments.getString("description") ?: ""

            Log.d("DayFragment","onCreate: ${_monthTitle} / ${_monthUUID} / ${_dayNumber}")
        }
        else {
            Log.d("DayFragment","No data available")
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(container == null){
            Log.d("DayFragment","no container selected")
        }
        if(savedInstanceState != null){
            Log.d("DayFragment","onCreateView : data available")
        }

        val fragmentView = inflater.inflate(R.layout.day_edit_fragment,container,false)
        val dateHeaderView : TextView = fragmentView.findViewById(R.id.txt_date)
        val eventEdit : EditText = fragmentView.findViewById(R.id.edit_event)
        val btnApply : Button = fragmentView.findViewById(R.id.btn_apply)

        eventEdit.setText(_description)
        dateHeaderView.setText("${_monthTitle}, ${_dayNumber}")

        Log.d("DayFragment","initializing view")
        if (fragmentView == null)
        {
            Log.d("DayFragment","cannot create view")
        }

        btnApply.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?){
                Log.d("DayFragment","Apply was clicked")
                _description = eventEdit.text.toString()
                val msgBundle = Bundle()
                msgBundle.putInt("monthNumber",_monthNumber)
                msgBundle.putString("monthUUID",_monthUUID.toString())
                msgBundle.putString("monthTitle",_monthTitle)
                msgBundle.putString("dayId",_dayId.toString())
                msgBundle.putInt("dayNumber",_dayNumber)
                msgBundle.putString("description",_description)
                ApplicationRepository.fragmentManager.setFragmentResult("dateModified",msgBundle)
            }
        })

        return fragmentView
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}
