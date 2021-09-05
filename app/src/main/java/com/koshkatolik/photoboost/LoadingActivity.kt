package com.koshkatolik.photoboost

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.anjlab.android.iab.v3.BillingProcessor


class LoadingActivity : AppCompatActivity() {
    private val TAG = "Permissions"
    var REQUEST_CODE_ASK_PERMISSIONS = 1
    lateinit var sharedSetting: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        sharedSetting = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isAvailable = BillingProcessor.isIabServiceAvailable(this)
        if(!isAvailable){
            Toast.makeText(this, "Ваше устройство не поддерживает Google Play!", Toast.LENGTH_SHORT).show()
            Log.e("Billing", "Loading (LoadingActivity): Устройство не поддерживает Google Play")
            finish()
        } else {
            if(!sharedSetting.getBoolean("hasLaunch", false)){
                Handler().postDelayed({ if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { requestPerms() } }, 2000)
            } else {
                startTimer(MainActivity::class.java)
            }
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
                Log.i(TAG, "Loading (LoadingActivity): Разрешения на камеру было получено")
                val e: SharedPreferences.Editor = sharedSetting.edit()
                e.putBoolean("hasLaunch", true)
                e.apply()
                startTimer(FirstLaunch1Activity::class.java)
            } else {
                Toast.makeText(this, "Для работы с приложением требуется доступ к мультимедии!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Loading (LoadingActivity): Разрешения на камеру были отклонены")
                finish()
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