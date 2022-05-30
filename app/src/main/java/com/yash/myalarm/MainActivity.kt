package com.yash.myalarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etTime : EditText
    lateinit var btnAlarm : Button

    private lateinit var etMTimePicker : EditText
    private lateinit var etDatePicker : EditText

    private val ALARM_REQUEST_CODE = 100

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        datePicker()
        val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        btnAlarm.setOnClickListener {

            val t = etTime.text.toString()
            val time = t.toInt()
//        var time = Integer.parseInt(etTime.text.toString())
            val triggerTime : Long = System.currentTimeMillis() + (time*1000)

            val iBroadCast = Intent(this,MyTimerReceiver::class.java)

            val pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime, pi)
        }

        etMTimePicker.setOnClickListener {

            mTimePicker()

        }


    }

    private fun initView(){
        btnAlarm = findViewById(R.id.btnTimer)
        etMTimePicker = findViewById(R.id.etMTimePicker)
        etDatePicker = findViewById(R.id.etDatePicker)
        etTime = findViewById(R.id.etTime)
    }

    private fun mTimePicker(){

        val mTimePicker : MaterialTimePicker

        // Check System Format
        val isSystem24Hour = DateFormat.is24HourFormat(this)
        // timePicker Format according to System Format
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // build the MaterialTimePicker Dialog
        mTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Time")
            .build()

        // Show the MaterialTimePicker
        mTimePicker.show(supportFragmentManager,"Boss")

        // Action by Positive response "OK"
        mTimePicker.addOnPositiveButtonClickListener {
            val hour = mTimePicker.hour
            val minute = mTimePicker.minute
            etMTimePicker.setText(String.format("%02d : %02d",hour,minute))
        }

    }

    // For Date Picker
    private var year = 0
    private var month = 0
    private var day = 0
    private fun datePicker() {
        val cal = Calendar.getInstance()
        val cal1 = Calendar.getInstance()

        year = cal1.get(Calendar.YEAR)
        month = cal1.get(Calendar.MONTH)
        day = cal1.get(Calendar.DAY_OF_MONTH)

        // For Creating Date Picker Dialog Using Dynamic Code
        val dateShow =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "dd-MM-yyyy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                etDatePicker.setText(dateFormat.format(cal.time))    // Set Date in EditText
//                val timeMills = cal1.timeInMillis
//                Log.d("toy","$timeMills")
            }

        // Date Piking When Click that EditText
        etDatePicker.setOnClickListener {
            val dateDialog = DatePickerDialog(
                this, dateShow,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            //for max date set (At current Time & Date)
            dateDialog.datePicker.maxDate = cal1.timeInMillis
            dateDialog.show()

        }

    }
}