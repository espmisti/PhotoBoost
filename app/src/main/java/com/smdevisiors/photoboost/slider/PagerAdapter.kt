package com.smdevisiors.photoboost.slider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smdevisiors.photoboost.fragments.sliders.FirstSlideFragment
import com.smdevisiors.photoboost.fragments.sliders.SecondSlideFragment
import com.smdevisiors.photoboost.fragments.sliders.ThirdSlideFragment

class PagerAdapter(fmManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fmManager, lifecycle) {
    var fragments:ArrayList<Fragment> = arrayListOf(FirstSlideFragment(), SecondSlideFragment(), ThirdSlideFragment())
    override fun getItemCount(): Int {
        return fragments.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}