package com.smdevisiors.photoboost

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class HistoryFragment : Fragment() {

    // Список со всеми фотками
    private var photosUrlList = ArrayList<String>()
    val TAG = "history"

    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        view.btnCloseHistory.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MainFragment())?.commit()
        }

// код истории без таймера
//        var mainActivity = MainActivity()
////        var mSettings: SharedPreferences = mainActivity.mSettings
//        val APP_PREFERENCES = "mysettings"
//        val MY_SHARED_PREF_NAME = "mySharedPref"
//
//        val sharedPref = activity?.getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
//        if (sharedPref.contains("list")) {
//            val stringList = sharedPref.getString("list", "у тебя не работает нихуя осел")
//            val gson = Gson()
//            val type = object : TypeToken<java.util.ArrayList<String?>?>() {}.type
//            photosUrlList = gson.fromJson(stringList, type) as ArrayList<String>
//        }
//
//        var bitmapUrlList = ArrayList<Bitmap?>(photosUrlList.size)
//        Log.i(TAG, "а потом = ${bitmapUrlList.size}")
//        for (i in 0 until photosUrlList.size) {
//            val bitmap = getBitmapFromURL(photosUrlList[i])
//            bitmapUrlList.add(bitmap)
////            getBitmapFromURL2(photosUrlList[i])
////            bitmapUrlList.add(image)
//            Log.i(TAG, "${bitmapUrlList.size}")
//        }

        // код истории с таймером
//        view?.findViewById<RecyclerView>(R.id.list_history)?.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
//        val adapter = HistoryAdapter(bitmapUrlList)
//        view?.findViewById<RecyclerView>(R.id.list_history)?.adapter = adapter
//
//        var bitmapUrlList = ArrayList<Bitmap?>(photosUrlList.size)
//        Log.i(TAG, "а потом = ${bitmapUrlList.size}")
//        thread {
//            for (i in 0 until photosUrlList.size) {
//                val bitmap = getBitmapFromURL(photosUrlList[i])
//                bitmapUrlList.add(bitmap)
//                //            getBitmapFromURL2(photosUrlList[i])
//                //            bitmapUrlList.add(image)
//                Log.i(TAG, "${bitmapUrlList.size}")
//            }
//        }
//        val timer = object: CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//            }
//
//            override fun onFinish() {
//                view?.findViewById<RecyclerView>(R.id.list_history)?.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
//                val adapter = HistoryAdapter(bitmapUrlList)
//                view?.findViewById<RecyclerView>(R.id.list_history)?.adapter = adapter
//                cancel()
//            }
//        }
//        timer.start()

        return view
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.i(TAG, "getBitmapFromURL: $e")
            return null
        }
    }

    private fun getBitmapFromURL2(src: String?) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            try {
                val url = URL(src)
                val bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                image = Bitmap.createScaledBitmap(bitMap, 100, 100, true)
            } catch (e: Exception) {
                Log.i("eblan", "getBitmapFromURL2: $e")
            }
        }
    }
}