package com.smdevisiors.photoboost

import android.graphics.Bitmap
import android.util.Log
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity


class MyWebViewClient(context: FragmentActivity?) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
        Log.d("MainActivity", "onPageStarted: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        Log.i("MainActivity", "$url + NEGRI")
        CookieSyncManager.getInstance().sync()
        super.onPageFinished(view, url)
    }
}
