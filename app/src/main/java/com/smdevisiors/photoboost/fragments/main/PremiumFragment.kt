package com.smdevisiors.photoboost.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smdevisiors.photoboost.R
import kotlinx.android.synthetic.main.fragment_premium.view.*

class PremiumFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_premium, container, false)
        view.p_button_buysearch.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, PremiumBuyCatalogFragment())?.commit()
        }
        return view
    }

}