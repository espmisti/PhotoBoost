package com.smdevisiors.photoboost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.smdevisiors.photoboost.subscriptions.BillingSubscribe
import kotlinx.android.synthetic.main.activity_choose_tarif.*

class ChooseTarifActivity : AppCompatActivity() {
    var billing = BillingSubscribe()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_tarif)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        billing.initialization(this)
        sub_firstTarif.setOnClickListener{
            billing.billing?.subscribe(this, "first")
        }
        sub_secondTarif.setOnClickListener {
            billing.billing?.subscribe(this, "second")
        }
        sub_buttonBack.setOnClickListener { finish() }
    }
}