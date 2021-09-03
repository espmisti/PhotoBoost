package com.koshkatolik.photoboost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import kotlinx.android.synthetic.main.activity_first_launch3.*

class FirstLaunch3Activity : AppCompatActivity(), BillingProcessor.IBillingHandler {

    private val TAG = "fl3"
    private var bp: BillingProcessor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch3)

        bp = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
        bp?.initialize()

//        Log.i(TAG, "one_purchase: ${bp?.getPurchaseListingDetails("one_purchase")}")
//        Log.i(TAG, "android.one_purchase: ${bp?.getPurchaseListingDetails("android.one_purchase")}")
//        Log.i(TAG, "com.koshkatolik.photoboost.one_purchase: ${bp?.getPurchaseListingDetails("com.koshkatolik.photoboost.one_purchase")}")
//        Log.i(TAG, "android.photoboost.one_purchase: ${bp?.getPurchaseListingDetails("android.photoboost.one_purchase")}")
//
        val bebraPurchase = "one_purchase"
        val bebraSubscription = "leonid"
//
//        val valera: SkuDetails? = bp?.getPurchaseListingDetails(bebraPurchase)
//        Log.i(TAG, "ВАЛЕРКА: ${valera?.productId}")

        fl3_button_best_price.setOnClickListener {
            bp?.purchase(this@FirstLaunch3Activity, bebraPurchase)
        }

        fl3_button_3_days_free.setOnClickListener {
            bp?.subscribe(this@FirstLaunch3Activity, bebraSubscription)
        }

        fl3_button_next.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        bp!!.release()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        Log.i(TAG, "удачно прошло хули сука ${details?.purchaseInfo?.responseData}")
        Toast.makeText(this, "удачно прошло хули сука $productId", Toast.LENGTH_SHORT).show()
    }

    override fun onPurchaseHistoryRestored() {
        Toast.makeText(this, "тут видимо чел оплатил а потом деньги вернул", Toast.LENGTH_SHORT).show()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(this, "ошибкаа хахаха", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "onBillingInitialized Error: ${error?.localizedMessage}, code: $errorCode")
    }

    override fun onBillingInitialized() {
        Log.i(TAG, "можно покупать короче я хуй знает")
//
//        Log.d("MainActivity", "onBillingInitialized: ")
//
//        bp!!.loadOwnedPurchasesFromGoogle()
//
//        fl3_button_3_days_free.setOnClickListener { v ->
//            if (bp!!.isSubscriptionUpdateSupported) {
//                bp!!.subscribe(this@FirstLaunch3Activity, "leonid")
//            } else {
//                Log.i(TAG, "onBillingInitialized: Subscription updated is not supported")
//            }
//        }

//        if (hasSubscription()) {
//            tvStatus.setText("Status: Premium")
//        } else {
//            tvStatus.setText("Status: Free")
//        }
    }
}