package com.smdevisiors.photoboost.fragments.main

import android.app.DownloadManager
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_google
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_premium
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_yandex
import kotlinx.android.synthetic.main.fragment_web_view.view.*
import android.webkit.WebView
import com.smdevisiors.photoboost.activities.MainActivity
import com.smdevisiors.photoboost.MyWebViewClient
import com.smdevisiors.photoboost.R


class WebViewFragment : Fragment() {

    lateinit var defWebView: WebView
    var SERVER_URL = "http://z96082yn.beget.tech/"
    private val TAG = "webview"
    lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        defWebView = view?.findViewById(R.id.webview_webview)!!
        val activity: MainActivity? = activity as MainActivity?
        val result: String? = activity?.urlGet()


        initView("https://yandex.ru/images/search?rpt=imageview&url=$SERVER_URL$result")
        view.webview_button_yandex.setOnClickListener {
            buttonNavigation(webview_button_yandex, webview_button_premium, webview_button_google)
            initView("https://yandex.ru/images/search?rpt=imageview&url=$SERVER_URL$result")
        }
        view.webview_button_google.setOnClickListener {
            buttonNavigation(webview_button_google, webview_button_yandex, webview_button_premium)
            initView("https://images.google.com/searchbyimage?image_url=$SERVER_URL$result")
        }
        view.webview_button_premium.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, PremiumSearchFragment())?.commit()
            buttonNavigation(webview_button_premium, webview_button_yandex, webview_button_google)
        }
        return view
    }


    private fun initView(url_site: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != requireActivity().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        CookieManager.getInstance().setAcceptCookie(true)
        defWebView.webViewClient = MyWebViewClient(activity)
        defWebView.settings.javaScriptEnabled = true
        defWebView.settings.loadWithOverviewMode = true //loads the WebView completely zoomed out
        defWebView.settings.useWideViewPort = true //makes the Webview have a normal viewport (such as a normal desktop browser), while when false the webview will have a viewport constrained to its own dimensions (so if the webview is 50px*50px the viewport will be the same size)
        defWebView.settings.setSupportZoom(true)
        defWebView.settings.builtInZoomControls = true //to remove the zoom buttons in webview
        defWebView.settings.displayZoomControls = false //to remove the zoom buttons in webview
        defWebView.settings.domStorageEnabled = true
        defWebView.settings.setAppCacheEnabled(true)
        defWebView.settings.loadsImagesAutomatically = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            defWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        defWebView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
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
            val dm = activity?.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            try {
                dm.enqueue(request)
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    e.message, Toast.LENGTH_SHORT
                ).show()
            }
        })
        defWebView.loadUrl(url_site)
    }
    // <-       Переключение кнопок        -> //
    fun buttonNavigation(active: TextView, unactive_1: TextView, unactive_2: TextView){
        try{
            active.setTextColor(requireActivity().getColor(R.color.active_search_service))
            unactive_1.setTextColor(requireActivity().getColor(R.color.unactive_search_service))
            unactive_2.setTextColor(requireActivity().getColor(R.color.unactive_search_service))
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "Ошибка в методе смены цвета кнопок: ", e)
        }

    }
}