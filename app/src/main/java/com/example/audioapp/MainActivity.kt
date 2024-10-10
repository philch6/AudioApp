package com.example.audioapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.pm.PackageManager
import android.widget.Button
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnRecord: Button
    private lateinit var mBtnPlay: Button
    private lateinit var mBtnStop: Button

    private var audioFilePath: String? = null
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mBtnRecord = findViewById(R.id.btnRecord)
        mBtnPlay   = findViewById(R.id.btnPlay)
        mBtnStop   = findViewById(R.id.btnStop)
    }

    private fun hasMicrophone(): Boolean {
        val pmanager = this.packageManager
        return pmanager.hasSystemFeature(
            PackageManager.FEATURE_MICROPHONE)
    }

    private fun audioSetup() {
        if (!hasMicrophone()) {
            mBtnStop.isEnabled = false
            mBtnPlay.isEnabled = false
            mBtnRecord.isEnabled = false
        } else {
            mBtnPlay.isEnabled = false
            mBtnStop.isEnabled = false
        }
        val audioFile = File(this.filesDir, "myaudio.3gp")
        audioFilePath = audioFile.absolutePath
    }
}