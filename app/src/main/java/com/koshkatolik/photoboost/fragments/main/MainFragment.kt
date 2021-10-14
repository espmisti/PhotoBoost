package com.koshkatolik.photoboost.fragments.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.koshkatolik.photoboost.ChooseTarifActivity
import com.koshkatolik.photoboost.R
import com.koshkatolik.photoboost.subscriptions.BillingClientWrapper
import com.koshkatolik.photoboost.subscriptions.BillingSubscribe
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    var billing = BillingSubscribe()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        billing.initialization(requireContext())
        //Log.i("sub_billing", "onCreateView: ${billing.isSub}")
//        val timer = object: CountDownTimer(3000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {}
//
//            override fun onFinish() {
//                if(billing.getStatusSubscribe()){
//                    view.m_text_subscription.text = "Подписка активна"
//                } else {
//                    view.m_text_subscription.text = "Подписки неактивна"
//                    view.m_text_subscription.setTextColor(Color.RED)
//                }
//            }
//        }
//        timer.start()

        view.m_button_choose_tariff.setOnClickListener { startActivity(Intent(requireActivity(), ChooseTarifActivity::class.java)) }
        return view
    }

}