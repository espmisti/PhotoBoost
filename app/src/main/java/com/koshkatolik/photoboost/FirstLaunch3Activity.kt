package com.koshkatolik.photoboost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.koshkatolik.photoboost.subscriptions.BillingSubscribe
import kotlinx.android.synthetic.main.activity_first_launch3.*


class FirstLaunch3Activity : AppCompatActivity() {

    private val TAG = "fl3"
//    var bp: BillingProcessor? = null
//    var purchaseTransactionDetails: TransactionDetails? = null
    var bp: BillingSubscribe = BillingSubscribe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch3)
        bp.initialization(this)
//        bp = BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
//        bp?.initialize()

        fl3_button_best_price.setOnClickListener {
//            billingWrapper.bp?.consumePurchase("one_purchase")
//            billingWrapper.bp?.purchase(this@FirstLaunch3Activity, "one_purchase")
        }
        fl3_button_3_days_free.setOnClickListener {
            //if(bp!!.isSubscriptionUpdateSupported){
            bp.billing?.subscribe(this@FirstLaunch3Activity, "leonid")
            //} else {
            //    Log.e(TAG, "onBillingInitialized: залупа", )
            //}
        }



        fl3_button_next.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    override fun onDestroy() {
        bp.billing?.release()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (bp.billing!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

//    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onPurchaseHistoryRestored() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBillingInitialized() {
//        //purchaseTransactionDetails = bp?.getSubscriptionTransactionDetails("leonid");
//
//        //bp?.loadOwnedPurchasesFromGoogle();
//        fl3_button_3_days_free.setOnClickListener {
//            //if(bp!!.isSubscriptionUpdateSupported){
//            bp.billing?.subscribe(this@FirstLaunch3Activity, "leonid")
//            //} else {
//            //    Log.e(TAG, "onBillingInitialized: залупа", )
//            //}
//        }
////        if (hasSub()) {
////            findViewById<TextView>(R.id.fl3_text2).text = "Status: Premium";
////        } else {
////            findViewById<TextView>(R.id.fl3_text2).text = "Status: Free";
////        }
//    }
}