package com.smdevisiors.photoboost

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.smdevisiors.photoboost.slider.PagerAdapter


class SliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        initViewPager()
    }
    private fun initViewPager(){
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)
    }

}