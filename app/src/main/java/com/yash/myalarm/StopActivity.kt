package com.yash.myalarm

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button

class StopActivity : AppCompatActivity() {

    private lateinit var btnStop : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop)
        btnStop = findViewById(R.id.btnStop)


        val mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mp.isLooping = true
        mp.start()

        btnStop.setOnClickListener {

            mp.stop()
            finish()

        }
    }
}