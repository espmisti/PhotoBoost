package com.example.photosearch

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient


class MyWebViewClient(context: Context) : WebViewClient() {
    var oh = ""
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
