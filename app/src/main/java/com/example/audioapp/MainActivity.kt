package com.example.audioapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import android.Manifest

class MainActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var mBtnRecord: Button
    private lateinit var mBtnPlay: Button
    private lateinit var mBtnStop: Button
    private val RECORD_REQUEST_CODE = 101
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

        audioSetup()
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
        requestPermission(Manifest.permission.RECORD_AUDIO,
            RECORD_REQUEST_CODE)
    }

    fun recordAudio(view: View) {
        isRecording = true
        mBtnStop.isEnabled = true
        mBtnPlay.isEnabled = false
        mBtnRecord.isEnabled = false

        try {
            mediaRecorder = MediaRecorder(this)
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(
                MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder?.setOutputFile(audioFilePath)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder?.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaRecorder?.start()
    }

    fun stopAudio(view: View) {
        mBtnStop.isEnabled = false
        mBtnPlay.isEnabled = true
        if (isRecording) {

            mBtnRecord.isEnabled = false
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
        } else {
            mediaPlayer?.release()
            mediaPlayer = null
            mBtnRecord.isEnabled = true
        }
    }

    fun playAudio(view: View) {

        mBtnStop.isEnabled = true
        mBtnPlay.isEnabled = false
        mBtnRecord.isEnabled = false
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(audioFilePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    private fun requestPermission(permissionType: String, requestCode: Int) {
        val permission = ContextCompat.checkSelfPermission(this,
            permissionType)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(permissionType), requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0]
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    mBtnRecord.isEnabled = false
                    Toast.makeText(
                        this,
                        "Record permission required",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}