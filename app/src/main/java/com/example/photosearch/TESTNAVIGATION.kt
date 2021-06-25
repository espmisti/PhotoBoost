package com.example.photosearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_testnavigation.*

class TESTNAVIGATION : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testnavigation)
        bottomNavView.background = null
        bottomNavView.menu.getItem(1).isEnabled = false
    }
}