package com.example.photosearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photosearch.api.ApiRequests
import com.example.photosearch.api.Response
import kotlinx.android.synthetic.main.fragment_premiun_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.*
import java.util.*
import kotlin.concurrent.thread


class PremiumSearchFragment : Fragment(), OnUserClickListener {

    private lateinit var comm: Communicator

    private val TAG = "vk"
    val BASE_URL = "https://api.vk.com"
    var users: ArrayList<OneVkUser> = ArrayList(10)
    var users2: ArrayList<Response> = ArrayList(10)
    var apiResponse: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: MainActivity? = activity as MainActivity?
        apiResponse = activity!!.getMyData()!!



//        thread {
//            if (isOnline()) {
//                setUsersList()
//            } else {
////                btn_update_list.visibility = View.VISIBLE
////                return@thread
//                Toast.makeText(contextf, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_premiun_search, container, false)

        if (isOnline()) {
            setUsersList()
        } else {
            view?.findViewById<ImageView>(R.id.btn_update_list)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.loading_progressbar)?.visibility = View.INVISIBLE
            Toast.makeText(context, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
            view?.findViewById<ImageView>(R.id.btn_update_list)?.setOnClickListener {
                if (isOnline()) {
                    btn_update_list.visibility = View.INVISIBLE
                    loading_progressbar.visibility = View.VISIBLE
                    setUsersList()
                } else {
                    Toast.makeText(context, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun setUsersList() {
        try {
            val json = try {
                JSONObject(apiResponse).getString("result")
            } catch (e: Exception) {
                "[]"
            }
            Log.i(TAG, "onCreate: $json")
            val timer = object: CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "onTick: ${users.size}")
                    if (json.equals("[]")) {
                        onFinish()
                    }
                    if (users.size == 10)
                        onFinish()
                }

                override fun onFinish() {
                    loading_progressbar.visibility = View.INVISIBLE
                    if (json.equals("[]")) {
                        ps_text_not_found.visibility = View.VISIBLE
                        val params: ViewGroup.LayoutParams = ps_rv_users.layoutParams
                        params.height = 0
                        ps_rv_users.layoutParams = params
                    } else {
                        ps_rv_users.layoutManager = CustomGridLayoutManager(context)
                    }
                    ps_rv_users.adapter = UsersAdapter(users, this@PremiumSearchFragment)
                    cancel()
                }
            }
            timer.start()

            if (!json.equals("[]")) {
                val result: Array<String> =
                    json.removeSurrounding("[", "]").split(",").toTypedArray()
                var listUsersId: ArrayList<String> = ArrayList(10)
                for (i in 0..9) {
                    listUsersId.add((result[i]))
                    getCurrentData(listUsersId[i])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "doEpta: $e")
        }
    }

    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

//    fun isOnline(): Boolean {
//        return try {
//            val timeoutMs = 1500
//            val sock = Socket()
//            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
//            sock.connect(sockaddr, timeoutMs)
//            sock.close()
//            Log.i(TAG, "ТЫ БЛЯ ОНЛАЙН НАХУЙ))")
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.i(TAG, "isOnline: $e")
//            false
//        }
//    }

    override fun onUserClickListener(position: Int) {
        var user = users[position]

        comm = requireActivity() as Communicator
        comm.passDataCom(user.userUrl)

//        val userFragment = UserWebViewFragment()
//
//        val bundle = Bundle()
//        bundle.putString("url", user.userUrl)
//        userFragment.arguments = bundle
//
//        val manager = activity?.supportFragmentManager
//        val userTransaction = manager?.beginTransaction()
//        userTransaction?.replace(R.id.fragment_container, UserWebViewFragment())
//        userTransaction?.commit()
//        Toast.makeText(context, "${user.userUrl}", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        ps_rv_users.layoutManager = LinearLayoutManager(context)
    }

//    private fun doEpta() : ArrayList<OneVkUser> {
//        var resultListUsers: ArrayList<OneVkUser> = ArrayList(10)
//        try {
//            val json = JSONObject(apiResponse).getString("result")
//            val result: Array<String> = json.removeSurrounding("[", "]").split(",").toTypedArray()
//            var listUsersId: ArrayList<String> = ArrayList(10)
//            for (i in 0..9) {
//                listUsersId.add((result[i]))
//                getCurrentData(listUsersId[i])
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.i(TAG, "doEpta: $e")
//        }
//        return resultListUsers
//    }

    private fun getCurrentData(id: String) {

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getUsers(id).awaitResponse()
                if (response.isSuccessful) {

                    val user = response.body()!!.response[0]
                    Log.i(TAG, user.toString())

                    val userUrl = "https://vk.com/id$id"
                    val imageUrl = user.photo_200
                    val name = user.first_name + " " + user.last_name
                    val vkUser = OneVkUser(getBitmapFromURL(imageUrl), name, userUrl)
                    users.add(vkUser)
//                    Log.i(TAG, "getVkUsers: ${vkUser.name}")
//                    Log.i(TAG, "getVkUsers: ${vkUser.bitmap}")
//                    Log.i(TAG, "getVkUsers: ${vkUser.userUrl}")
//                    Log.i(TAG, "------------------------------------")

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(TAG, "getCurrentData: $e")
            }
        }
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

}


//            thread {
//                for (i in 0..9) {
//                    var userUrl = "https://vk.com/id${listUsersId[i]}"
//                    val docBanned = Jsoup.connect(userUrl).get()
////                    var imageUrl = docBanned.select("div[class=page_avatar]").select("img").attr("src")
//                    var imageUrl = docBanned.select("div[class=page_avatar]").select("img[class=page_avatar_img]").attr("src")
//                    var imageUrlTag = docBanned.select("div[class=page_avatar]").select("img[class=page_avatar_img]").toString()
//                    Log.i(TAG, "doEpta: $imageUrlTag")
//                    var bannedStatus = false
//                    // || imageUrl.equals("/images/deactivated_200.gif")
//                    //                        || imageUrl.equals("/images/camera_200.png")
//                    if (imageUrl.equals("/images/deactivated_hid_200.gif")
//                        ) {
//                        imageUrl = ""
//                        bannedStatus = true
//                    }
//                    val name = docBanned.select("div[class=page_top]").select("h1").text()
//                    var user = OneVkUser(getBitmapFromURL(imageUrl), bannedStatus,  name, userUrl)
//                    resultListUsers.add(user)
//                    Log.i(TAG, "getVkUsers: ${user.name}")
//                    Log.i(TAG, "getVkUsers: ${user.bannedStatus}")
//                    Log.i(TAG, "getVkUsers: ${user.bitmap}")
//                    Log.i(TAG, "getVkUsers: ${user.userUrl}")
//                    Log.i(TAG, "------------------------------------")
//                }
//            }