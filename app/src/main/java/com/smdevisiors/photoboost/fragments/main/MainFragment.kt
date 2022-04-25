package com.smdevisiors.photoboost.fragments.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smdevisiors.photoboost.ChooseTarifActivity
import com.smdevisiors.photoboost.R
import com.smdevisiors.photoboost.subscriptions.BillingSubscribe
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    var billing = BillingSubscribe()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        billing.initialization(requireContext())
        val timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if(billing.getSub("first") || billing.getSub("second")){
                    view.m_text_subscription.text = "Подписка активна"
                } else {
                    view.m_text_subscription.text = "Подписка неактивна"
                    view.m_text_subscription.setTextColor(Color.RED)
                }
            }
        }
        timer.start()

        view.m_button_choose_tariff.setOnClickListener { startActivity(Intent(requireActivity(), ChooseTarifActivity::class.java)) }
        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billing.billing?.handleActivityResult(requestCode, resultCode, data)!!) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}