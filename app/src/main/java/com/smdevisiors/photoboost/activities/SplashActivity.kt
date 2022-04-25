package com.smdevisiors.photoboost.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smdevisiors.photoboost.R
import com.smdevisiors.photoboost.SliderActivity
import com.smdevisiors.photoboost.components.ScreenSetting


class SplashActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ScreenSetting().setFull(window)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, SliderActivity::class.java))
                    Log.i("SPLASH-log", "[Permission]: Разрешения на камеру было получено")
                } else {
                    Log.e("SPLASH-log", "[Permission]: Разрешения на камеру были отклонены")
                    finish()
                }
        }
    }
}