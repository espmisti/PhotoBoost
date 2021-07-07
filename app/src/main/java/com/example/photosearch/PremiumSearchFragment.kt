package com.example.photosearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var peoplesIdJsonStr = "{\"result\": \"[270564122,425014045,444696163,337866445,418154833,404682438,201682880,152492373,268011376,355852818,420868235,140517386,477780780,58731986,61166233,226641182,209744485,165567710,235651656,281112559,275678606,399542695,239092894,398641210,301148945,345994134,326047570,400189637,340890823,177071636,386569363,345551375,346216166,238286570,387547217,213467028,291073876,428793640,421009066,223404305,398400285,418153818,107232292,314154362,242651323,280721295,343287203,369268837,422916076,185134031,133796518,392132590,272897442,55901750,262058698,450119886,152891699,226254496,420611364,334705088,85084754,404160635,195204026,347325873,246203744,393953757,438370734,206346133,74168080,245125508,157321658,202246555,391422434,413267326,383128806,227318413,432767886,304339522,333966067,434344776,308153848,203438172,420012848,248760694,181688419,473296887,298861949,218397653,192137111,155305822,156011087,215257016,278182469,395780104,325843151,432479149,391624802,352399658,262734792,314280576,274458934,256392504,337144737,165988270,236950323,325556757,453229629,102248330,398979622,22495653,460407463,315496014,217413556,358948948,312963521,418260517,319883070,172861898,368088184,393325872,275269986,285914411,422028896,427662754,317670532,365188662,376100522,241244581]\"}"

        users = doEpta(peoplesIdJsonStr)

        val timer = object: CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                loading.visibility = View.INVISIBLE
                rv_users.adapter = UsersAdapter(users, this@PremiumSearchFragment)
            }
        }
        timer.start()
    }

    override fun onUserClickListener(position: Int) {
        var user = users[position]
        Toast.makeText(context, "${user.userUrl}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_premiun_search, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()

        val customLayoutManager = CustomGridLayoutManager(context)

        rv_users.layoutManager = customLayoutManager

    }

    // сюда засунуть бля работу с апи
    private fun getVkUsers() : String{
        return ""
    }

    private fun doEpta(usersId: String) : ArrayList<OneVkUser> {
        var resultListUsers: ArrayList<OneVkUser> = ArrayList(10)
        try {
            val jsonStr = usersId.substring(0, 11) + usersId.substring(12, usersId.length - 2) + "}"
            val json = JSONObject(jsonStr)
            val jsonListUsers = json.getJSONArray("result")
            var listUsersId: ArrayList<String> = ArrayList(10)
            for (i in 0..9) {
                listUsersId.add((jsonListUsers.get(i) as Int).toString())
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
                    val user = OneVkUser(getBitmapFromURL(imageUrl), bannedStatus,  name, userUrl)
                    resultListUsers.add(user)
                    Log.i(TAG, "getVkUsers: ${user.name}")
                    Log.i(TAG, "getVkUsers: ${user.bannedStatus}")
                    Log.i(TAG, "getVkUsers: ${user.bitmap}")
                    Log.i(TAG, "getVkUsers: ${user.userUrl}")
                    Log.i(TAG, "------------------------------------")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "doEpta: $e")
        }
        return resultListUsers
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
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