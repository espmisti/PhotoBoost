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
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_google
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_premium
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_tineye
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_button_yandex
import kotlinx.android.synthetic.main.activity_photo_web_view.webview_webview
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.view.*

class WebViewFragment : Fragment() {
    var defURL = "https://yandex.ru/images/search?rpt=imageview&url="
    var SERVER_URL = "http://z96082yn.beget.tech/"
    val TAG = "webview"
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
        val result: String? = activity?.urlGet()
        //initView("tanya.com")
        //initView("https://yandex.ru/images/search?rpt=imageview&url=$SERVER_URL$result")
        view.webview_button_yandex.setOnClickListener {
            buttonNavigation(webview_button_yandex, webview_button_premium, webview_button_google, webview_button_tineye)
            initView("https://yandex.ru/images/search?rpt=imageview&url=$SERVER_URL$result")
        }
        view.webview_button_google.setOnClickListener {
            buttonNavigation(webview_button_google, webview_button_yandex, webview_button_premium, webview_button_tineye)
            initView("https://images.google.com/searchbyimage?image_url=$SERVER_URL$result")
        }
        view.webview_button_tineye.setOnClickListener {
            buttonNavigation(webview_button_tineye, webview_button_yandex, webview_button_google, webview_button_premium)
            initView("https://tineye.com/")
        }
        view.webview_button_premium.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, PremiumSearchFragment())?.commit()
            buttonNavigation(webview_button_premium, webview_button_yandex, webview_button_google, webview_button_tineye)
        }

        val myDataFromActivity: String? = activity!!.getMyData()
        Toast.makeText(context, "$myDataFromActivity", Toast.LENGTH_SHORT).show()

        return view
    }

    private fun initView(url_site: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != requireActivity().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        CookieManager.getInstance().setAcceptCookie(true)
        webview_webview.webViewClient = MyWebViewClient(activity)
        webview_webview.settings.javaScriptEnabled = true
        webview_webview.settings.loadWithOverviewMode = true //loads the WebView completely zoomed out
        webview_webview.settings.useWideViewPort = true //makes the Webview have a normal viewport (such as a normal desktop browser), while when false the webview will have a viewport constrained to its own dimensions (so if the webview is 50px*50px the viewport will be the same size)
        webview_webview.settings.setSupportZoom(true)
        webview_webview.settings.builtInZoomControls = true //to remove the zoom buttons in webview
        webview_webview.settings.displayZoomControls = false //to remove the zoom buttons in webview
        webview_webview.settings.domStorageEnabled = true
        webview_webview.settings.setAppCacheEnabled(true)
        webview_webview.settings.loadsImagesAutomatically = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview_webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
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
        if (url_site.isNotEmpty()) {
            webview_webview.loadUrl(url_site)
        } else {
            webview_webview.loadUrl(shared!!.getString("url", "")!!)
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