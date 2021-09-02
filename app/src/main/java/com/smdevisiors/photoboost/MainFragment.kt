package com.smdevisiors.photoboost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    val isSubscribe: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

//        Toast.makeText(view.context, "ты мужик в фрагменте мейн", Toast.LENGTH_SHORT).show()

        view.m_button_choose_tariff.setOnClickListener {
            startActivity(Intent(activity, FirstLaunch3Activity::class.java))
            activity?.finish()
        }

//        view.m_button_history.setOnClickListener {
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.fragment_container, HistoryFragment())?.commit()
//        }


        return view
    }

}