package com.example.photosearch

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import android.os.Build




class LoadingActivity : AppCompatActivity() {
    private val TAG = "Permissions"
    var REQUEST_CODE_ASK_PERMISSIONS = 1
    lateinit var sharedSetting: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        sharedSetting = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if(!sharedSetting.getBoolean("hasLaunch", false)){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { requestPerms() }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { requestPerms() }
            val e: SharedPreferences.Editor = sharedSetting.edit()
            e.putBoolean("hasLaunch", true)
            e.apply()
        } else {
            startTimer(MainActivity::class.java)
        }
    }

    fun startTimer(arg: Class<*>){
        Handler().postDelayed({
            startActivity(Intent(this, arg))
            finish()
        }, 2000)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permissions granted")
                startTimer(FirstLaunch1Activity::class.java)
            } else {
                Log.e(TAG, "Permissions denied")
                startTimer(FirstLaunch1Activity::class.java)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    private fun requestPerms() {
        val perm = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, perm, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }
}