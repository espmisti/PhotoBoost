package com.example.photosearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_premiun_search.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread


class PremiumSearchFragment : Fragment(), OnUserClickListener {

    private val TAG = "vk"
    var users: ArrayList<OneVkUser> = ArrayList(10)
    var apiResponse: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: MainActivity? = activity as MainActivity?
        apiResponse = activity!!.getMyData()!!
        users = doEpta()

        val timer = object: CountDownTimer(13000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "onTick: ${users.size}")
            }

            override fun onFinish() {
                loading_progressbar.visibility = View.INVISIBLE
                ps_rv_users.layoutManager = CustomGridLayoutManager(context)
                ps_rv_users.adapter = UsersAdapter(users, this@PremiumSearchFragment)
            }
        }
        timer.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_premiun_search, container, false)

        return view
    }

    override fun onUserClickListener(position: Int) {
        var user = users[position]
        Toast.makeText(context, "${user.userUrl}", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        val customLayoutManager = CustomGridLayoutManager(context)

        ps_rv_users.layoutManager = LinearLayoutManager(context)

    }

    private fun doEpta() : ArrayList<OneVkUser> {
        var resultListUsers: ArrayList<OneVkUser> = ArrayList(10)
        try {
            val json = JSONObject(apiResponse).getString("result")
            if (json == "[]") {
                Log.i(TAG, "doEpta: чел ну тут пусто нахуй")
//                ps_text_not_found.visibility = View.VISIBLE
//                ic_loading.visibility = View.INVISIBLE
//                ps_rv_users.layoutManager = LinearLayoutManager(context)
            }
            val result: Array<String> = json.removeSurrounding("[", "]").split(",").toTypedArray()
            var listUsersId: ArrayList<String> = ArrayList(10)
            for (i in 0..9) {
                listUsersId.add((result[i]))
            }

            thread {
                for (i in 0..9) {
                    var userUrl = "https://vk.com/id${listUsersId[i]}"
                    val docBanned = Jsoup.connect(userUrl).get()
                    var imageUrl = docBanned.select("div[class=page_avatar]").select("img").attr("src")
                    var bannedStatus = false
                    if (imageUrl.equals("/images/deactivated_hid_200.gif")) {
                        imageUrl = ""
                        bannedStatus = true
                    }
                    val name = docBanned.select("div[class=page_top]").select("h1").text()
                    var user = OneVkUser(getBitmapFromURL(imageUrl), bannedStatus,  name, userUrl)
                    resultListUsers.add(user)
                    Log.i(TAG, "getVkUsers: ${user!!.name}")
                    Log.i(TAG, "getVkUsers: ${user!!.bannedStatus}")
                    Log.i(TAG, "getVkUsers: ${user!!.bitmap}")
                    Log.i(TAG, "getVkUsers: ${user!!.userUrl}")
                    Log.i(TAG, "------------------------------------")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "doEpta: $e")
        }
        return resultListUsers
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        run {
            return try {
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                Log.i(TAG, "getBitmapFromURL: $e")
                return null
            }
        }
    }

}