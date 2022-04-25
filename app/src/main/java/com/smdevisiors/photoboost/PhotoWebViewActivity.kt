package com.smdevisiors.photoboost

import android.app.DownloadManager
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_photo_web_view.*

class PhotoWebViewActivity : AppCompatActivity() {
    private var shared: SharedPreferences? = null
    var TEST_STRING = "https://yandex.ru/images/search"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_web_view)

        var uriImage = intent.getStringExtra("uri")
        Log.i("webvieiwiew", "onCreate: $uriImage")
        // yandex search = https://yandex.ru/images/search?rpt=imageview&url=http://copp63.ru/images/promo.svg?2232323

        shared = getSharedPreferences("APP", MODE_PRIVATE)
        if (isNetworkConnected) {
            initView("http://images.google.com/searchbyimage?image_url=$uriImage")
        } else {
            layout?.setVisibility(View.GONE)
            //     progressBar.setVisibility(View.GONE);
        }

        navigationButtons()
    }

    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    private fun initView(url_site: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        CookieManager.getInstance().setAcceptCookie(true)
        webview_webview.setWebViewClient(MyWebViewClient(this@PhotoWebViewActivity))
        webview_webview.settings.javaScriptEnabled = true
        webview_webview.settings.loadWithOverviewMode = true //loads the WebView completely zoomed out
        webview_webview.settings.useWideViewPort =
            true //makes the Webview have a normal viewport (such as a normal desktop browser), while when false the webview will have a viewport constrained to its own dimensions (so if the webview is 50px*50px the viewport will be the same size)
        webview_webview.getSettings().setSupportZoom(true)
        webview_webview.getSettings().builtInZoomControls = true //to remove the zoom buttons in webview
        webview_webview.getSettings().displayZoomControls = false //to remove the zoom buttons in webview
        webview_webview.getSettings().domStorageEnabled = true
        webview_webview.getSettings().setAppCacheEnabled(true)
        webview_webview.getSettings().loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview_webview.getSettings().mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webview_webview.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimetype)
            val cookies = CookieManager.getInstance().getCookie(url)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Description")
            request.setTitle(
                URLUtil.guessFileName(
                    url, contentDisposition,
                    mimetype
                )
            )
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(url, contentDisposition, mimetype)
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            try {
                dm.enqueue(request)
            } catch (e: Exception) {
                Toast.makeText(
                    this@PhotoWebViewActivity,
                    e.message, Toast.LENGTH_SHORT
                ).show()
            }
        })
        if (!TEST_STRING.isEmpty()) {
            webview_webview.loadUrl(url_site)
            TEST_STRING = url_site
        } else {
            webview_webview.loadUrl(shared!!.getString("url", "")!!)
        }
    }

    override fun onBackPressed() {
        if (webview_webview!!.canGoBack()) {
            webview_webview!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // меняет цвет активной кнопки и цвета неактивных
    private fun navigationButtons() {
        webview_button_yandex.setOnClickListener {
            webview_button_yandex.setTextColor(getColor(R.color.active_search_service))
            initView("https://yandex.ru/images/search")
            //
            webview_button_google.setTextColor(getColor(R.color.unactive_search_service))
            webview_button_premium.setTextColor(getColor(R.color.unactive_search_service))
        }

        webview_button_google.setOnClickListener {
            webview_button_yandex.setTextColor(getColor(R.color.unactive_search_service))
            //
            webview_button_google.setTextColor(getColor(R.color.active_search_service))
            initView("https://images.google.ru/")
            //
            webview_button_premium.setTextColor(getColor(R.color.unactive_search_service))
        }


        webview_button_premium.setOnClickListener {
            webview_button_yandex.setTextColor(getColor(R.color.unactive_search_service))
            webview_button_google.setTextColor(getColor(R.color.unactive_search_service))
            //
            webview_button_premium.setTextColor(getColor(R.color.active_search_service))
            initView("https://vk.com/negrimama")
        }
    }
}