package com.example.photosearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_premium.*

class PremiumFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var selectedFragment: Fragment? = null
//        p_buttonBuySearch.setOnClickListener {
//            selectedFragment = BuyCatalogFragment()
//        }

        return inflater.inflate(R.layout.fragment_premium, container, false)

    }

}