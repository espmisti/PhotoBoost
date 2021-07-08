package com.example.photosearch

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
import kotlinx.android.synthetic.main.activity_photo_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.view.*


class WebViewFragment : Fragment() {
    var TEST_STRING = "https://yandex.ru/images/search"
    var SERVER_URL = "https://u724370.com4.ru/"
    private var shared: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        val activity: MainActivity? = activity as MainActivity?
        val resutl: String? = activity?.urlGet()
        Log.i("toosodo", "onCreateView: $resutl")
        view.btnYandex.setOnClickListener {
            buttonNavigation(btnYandex, btnPremiunSearch, btnGoogle, btnTinEye)
            initView("https://yandex.ru/images/search?rpt=imageview&url=$SERVER_URL$resutl")
        }
        view.btnGoogle.setOnClickListener {
            buttonNavigation(btnGoogle, btnYandex, btnPremiunSearch, btnTinEye)
            initView("https://images.google.com/searchbyimage?image_url=$SERVER_URL$resutl")
        }
        view.btnTinEye.setOnClickListener {
            buttonNavigation(btnTinEye, btnYandex, btnGoogle, btnPremiunSearch)
            initView("https://tineye.com/")
        }
        view.btnPremiunSearch.setOnClickListener {
            buttonNavigation(btnPremiunSearch, btnYandex, btnGoogle, btnTinEye)
            initView("https://vk.com/negrimama")
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
        mWebView.setWebViewClient(MyWebViewClient(activity))
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.loadWithOverviewMode = true //loads the WebView completely zoomed out
        mWebView.settings.useWideViewPort =
            true //makes the Webview have a normal viewport (such as a normal desktop browser), while when false the webview will have a viewport constrained to its own dimensions (so if the webview is 50px*50px the viewport will be the same size)
        mWebView.getSettings().setSupportZoom(true)
        mWebView.getSettings().builtInZoomControls = true //to remove the zoom buttons in webview
        mWebView.getSettings().displayZoomControls = false //to remove the zoom buttons in webview
        mWebView.getSettings().domStorageEnabled = true
        mWebView.getSettings().setAppCacheEnabled(true)
        mWebView.getSettings().loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        mWebView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
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
        if (!TEST_STRING.isEmpty()) {
            mWebView.loadUrl(url_site)
            TEST_STRING = url_site
        } else {
            mWebView.loadUrl(shared!!.getString("url", "")!!)
        }
    }

    // <-       Переключение кнопок        -> //
    fun buttonNavigation(active: TextView, unactive_1: TextView, unactive_2: TextView, unactive_3: TextView){
        active.setTextColor(requireActivity().getColor(R.color.active_search_service))
        unactive_1.setTextColor(requireActivity().getColor(R.color.unactive_search_service))
        unactive_2.setTextColor(requireActivity().getColor(R.color.unactive_search_service))
        unactive_3.setTextColor(requireActivity().getColor(R.color.unactive_search_service))
    }

}