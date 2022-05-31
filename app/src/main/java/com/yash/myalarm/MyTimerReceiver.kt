package com.yash.myalarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyTimerReceiver : BroadcastReceiver() {

//    private lateinit var mp : MediaPlayer
    override fun onReceive(context : Context?, intent : Intent?) {

//        mp = MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI)
//        mp.isLooping = true
//        mp.start()

        val i = Intent(context,StopActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context?.startActivity(i)

        val pendingIntent = PendingIntent.getActivity(context,0,i,0)

        val builder = NotificationCompat.Builder(context!!,"alarmId")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Alarm Reminder")
            .setContentText("Channel For Alarm Manager")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())
    }

}