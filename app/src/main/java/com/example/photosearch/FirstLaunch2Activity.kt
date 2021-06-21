package com.example.photosearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class FirstLaunch2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch2)
        (findViewById<ImageView>(R.id.fl2_buttonClose)).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            finish()
        }
        (findViewById<Button>(R.id.fl2_button_next1)).setOnClickListener { startActivity(Intent(this, FirstLaunch3Activity::class.java)) }
    }
}