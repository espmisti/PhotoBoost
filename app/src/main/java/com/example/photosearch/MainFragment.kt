package com.example.photosearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        view.f_main_button_choose_tariff.setOnClickListener {
            Toast.makeText(activity, "asdasd", Toast.LENGTH_SHORT).show() }
        view.m_button_history.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, HistoryFragment())?.commit()
        }
        return view
    }
}