package com.koshkatolik.photoboost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.koshkatolik.photoboost.subscriptions.BillingClientWrapper
import kotlinx.android.synthetic.main.activity_first_launch3.*

class FirstLaunch3Activity : AppCompatActivity(){

    private val TAG = "fl3"
    val billingWrapper:BillingClientWrapper = BillingClientWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch3)

        billingWrapper.turboMisha(this)



        fl3_button_best_price.setOnClickListener {
            billingWrapper.bp?.consumePurchase("one_purchase")
            billingWrapper.bp?.purchase(this@FirstLaunch3Activity, "one_purchase")
        }

        fl3_button_3_days_free.setOnClickListener {
            billingWrapper.bp?.subscribe(this@FirstLaunch3Activity, "leonid")
        }

        fl3_button_next.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        billingWrapper.bp!!.release()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingWrapper.bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}