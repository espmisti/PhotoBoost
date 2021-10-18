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
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.koshkatolik.photoboost.subscriptions.BillingSubscribe


class LoadingActivity : AppCompatActivity(), BillingProcessor.IBillingHandler{
    private val TAG = "LOADING"
    var billing: BillingProcessor? = null
    var REQUEST_CODE_ASK_PERMISSIONS = 1
    lateinit var sharedSetting: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        ini()
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
                startTimer(SliderActivity::class.java)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billing?.handleActivityResult(requestCode, resultCode, data)!!) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        TODO("Not yet implemented")
    }

    override fun onPurchaseHistoryRestored() {
        TODO("Not yet implemented")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun onBillingInitialized() {
        billing?.loadOwnedPurchasesFromGoogle()
        if(isHasSubscribe(billing, "first"))
            Log.i(TAG, "Подписка есть на first")
        else
            Log.i(TAG, "Подписки нету на first")
        if(isHasSubscribe(billing, "second"))
            Log.i(TAG, "Подписка есть на second")
        else
            Log.i(TAG, "Подписки нету на second")
    }

    private fun ini(){
        billing = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
        billing?.initialize()
    }

    private fun isHasSubscribe(billingProcessor: BillingProcessor?, productId: String): Boolean {
        if(billingProcessor?.getSubscriptionTransactionDetails(productId) != null){
            return (billingProcessor.getSubscriptionTransactionDetails(productId))?.purchaseInfo != null
        }
        return false
    }
}