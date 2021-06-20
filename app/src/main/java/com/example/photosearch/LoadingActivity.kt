package com.example.photosearch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingActivity : AppCompatActivity() {
    lateinit var sharedSetting: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        sharedSetting = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if(sharedSetting.getBoolean("hasLaunch", false)){
            // first launch
            val e: SharedPreferences.Editor = sharedSetting.edit()
            e.putBoolean("hasLaunch", true)
            e.apply()
            startTimer(MainActivity::class.java)
        } else {
            // no first launch
            startTimer(FirstLaunch1Activity::class.java)
        }
    }

    fun startTimer(arg: Class<*>){
        Handler().postDelayed({
            startActivity(Intent(this, arg))
            finish()
        }, 2000)
    }
}