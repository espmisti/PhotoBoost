package com.koshkatolik.photoboost

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koshkatolik.photoboost.subscriptions.BillingClientWrapper
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    var billingWrapper: BillingClientWrapper = BillingClientWrapper()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        billingWrapper.turboMisha(activity)

        if(billingWrapper.getSub()){ view.findViewById<TextView>(R.id.m_text_subscription).text = "Подписка активна" }
        else {
            view.findViewById<TextView>(R.id.m_text_subscription).text = "Подписка неактивная"
            view.findViewById<TextView>(R.id.m_text_subscription).setTextColor(Color.RED)
        }

        view.m_button_choose_tariff.setOnClickListener {
            startActivity(Intent(activity, FirstLaunch3Activity::class.java))
            activity?.finish()
        }
        return view
    }

}