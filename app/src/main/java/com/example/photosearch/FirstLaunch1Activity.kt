package com.example.photosearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class FirstLaunch1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch1)
        (findViewById<ImageView>(R.id.fl_buttonClose)).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        (findViewById<Button>(R.id.fl_button_next1)).setOnClickListener { startActivity(Intent(this, FirstLaunch2Activity::class.java)) }
    }
}