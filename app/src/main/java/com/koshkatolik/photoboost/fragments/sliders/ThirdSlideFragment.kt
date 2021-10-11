package com.koshkatolik.photoboost.fragments.sliders

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koshkatolik.photoboost.MainActivity
import com.koshkatolik.photoboost.R
import kotlinx.android.synthetic.main.fragment_third_slide.view.*

class ThirdSlideFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third_slide, container, false)
        view.thirdSlide_button.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
        return view
    }
}