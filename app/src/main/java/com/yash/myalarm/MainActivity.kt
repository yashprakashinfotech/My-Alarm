package com.yash.myalarm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etTime : EditText
    private lateinit var btnTimer : Button
    private lateinit var btnAlarm : Button
    private lateinit var btnCancel : Button

    private lateinit var etMTimePicker : EditText
    private lateinit var etDatePicker : EditText

    lateinit var myAlarm :MyAlarmReceiver

    private val ALARM_REQUEST_CODE = 100

    lateinit var alarmManager : AlarmManager
    lateinit var pi : PendingIntent
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        createNotificationChannel()

        datePicker()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        btnTimer.setOnClickListener {

            val t = etTime.text.toString()
            val time = t.toInt()
//        var time = Integer.parseInt(etTime.text.toString())
            val triggerTime : Long = System.currentTimeMillis() + (time*1000)

            val iBroadCast = Intent(this,MyTimerReceiver::class.java)

            val pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime, pi)
        }

        btnAlarm.setOnClickListener {

            val etDate = etDatePicker.text.toString()
            val etTime = etMTimePicker.text.toString()

            if (etDate.isNotEmpty() && etTime.isNotEmpty()){

                val myDate = "$etDate $etTime:00"
//            val myDate = "2014/10/29 18:10:45"
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = sdf.parse(myDate)
                val millis = date?.time
                Log.d("boss","$millis")

                val iBroadCast = Intent(this,MyTimerReceiver::class.java)

                pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)

                alarmManager.set(AlarmManager.RTC_WAKEUP,millis!!, pi)
                Toast.makeText(this,"Alarm Set!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Please select Date And Time!",Toast.LENGTH_SHORT).show()
            }

        }
        btnCancel.setOnClickListener {



            val intent = Intent(this,MyAlarmReceiver::class.java)
            pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,intent,0)
            alarmManager.cancel(pi)
            Toast.makeText(this,"Alarm Cancelled",Toast.LENGTH_SHORT).show()
        }


        etMTimePicker.setOnClickListener {

            mTimePicker()

        }


    }

    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name : CharSequence = "Alarm Reminder"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarmId",name,importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initView(){
        btnTimer = findViewById(R.id.btnTimer)
        btnAlarm = findViewById(R.id.btnAlarm)
        btnCancel = findViewById(R.id.btnCancel)
        etMTimePicker = findViewById(R.id.etMTimePicker)
        etDatePicker = findViewById(R.id.etDatePicker)
        etTime = findViewById(R.id.etTime)

        myAlarm = MyAlarmReceiver()
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
            etMTimePicker.setText(String.format("%02d:%02d",hour,minute))
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
                val myFormat = "yyyy-MM-dd"
//                val myFormat = "dd-MM-yyyy"
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
            dateDialog.datePicker.minDate = cal1.timeInMillis
            dateDialog.show()

        }

    }
}