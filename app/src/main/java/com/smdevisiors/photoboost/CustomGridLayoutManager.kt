package com.smdevisiors.photoboost

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

open class CustomGridLayoutManager(context: Context?) : LinearLayoutManager(context) {
    private var isScrollEnabled = false

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}