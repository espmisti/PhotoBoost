package com.koshkatolik.photoboost

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.koshkatolik.photoboost.Helpers.makeRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koshkatolik.photoboost.fragments.main.*
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.net.ftp.FTPClient
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), Communicator {

    private val REQUEST_CODE = 42
    val TAG = "eblan"
    lateinit var dialogChoose: Dialog
    val BASE_URL = "https://data.av100.ru"

    val appContext: Context? = this

    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_CODE_CAMERA = 2

    private val SERVER_IP = "z96082yn.beget.tech"
    private val SERVER_USERNAME = "z96082yn"
    private val SERVER_PASSWORD = "q1w2e3r4t5y6u7"

    private val SERVER_DIR = "z96082yn.beget.tech/public_html"

    private val ftp = ftp_client()
    var fileURL = ""
    var result: String? = null

    // Список со всеми фотками
    private var photosUrlList = ArrayList<String>()

    // SharedPreferences
    lateinit var mSettings: SharedPreferences
    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_NAME = "HistoryListUrl"
    val MY_SHARED_PREF_NAME = "mySharedPref"

//    private val purchasesUpdatedListener =
//        PurchasesUpdatedListener { billingResult, purchases ->
//            // To be implemented in a later section.
//        }
//
//    private var billingClient = BillingClient.newBuilder(this)
//        .setListener(purchasesUpdatedListener)
//        .enablePendingPurchases()
//        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // SharedPreferences
//        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = mSettings.edit()
//        editor.putString(APP_PREFERENCES_NAME, photosUrlList)
//        editor.apply()
//        // SharedPreferences

//
        showSavedUrl()

            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        navigationView2.itemIconTintList = null
//
//        val bundle = Bundle()
//        if (mSettings.contains(APP_PREFERENCES_NAME)) {
//            val photosList = mSettings.getString(APP_PREFERENCES_NAME, "")
//            bundle.putStringArrayList("arrayPhotos", photosList)
//            fragment.setArguments(bundle)
//        } else {
//
//        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
        Log.i("history", "Изначально = ${photosUrlList.size}")

        dialogChoose = Dialog(this)
        btnSearch.setOnClickListener {
// openGalleryForImage()
            openDialog()
            //urlGet()
        }

        btnSearch.setOnClickListener { openDialog() }

        var selectedFragment: Fragment? = null
        //textDescMain.text = Html.fromHtml("<u>Подписка активна<br></u>") - выебывается твой код
        bottonNavigatorView.background = null
        floatbar_bg.isEnabled = false

        bottonNavigatorView.setOnNavigationItemSelectedListener{
            item -> when (item.itemId){
                R.id.mPremium ->{ selectedFragment = PremiumFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment!!).commit() }
                R.id.mMenu -> { drawerLayout.openDrawer(GravityCompat.START) }
            }
            true
        }
    }

    // <-       Работа с Галереей/Камерой       -> //

    private fun openGalleryForImage() {
        try{

            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, REQUEST_CODE_GALLERY)
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG,"openGalleryForImage: ", e)
            Toast.makeText(this, "Ваше приложение временно не поддерживать поиск через галерею!", Toast.LENGTH_SHORT).show()
        }
    }
    var fileName = ""
    private fun openCameraForImage(){
        try{
            fileName = generateFileName()
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = File("storage/emulated/0/DCIM/Camera/$fileName.jpg")
            val outputFileUri = Uri.fromFile(file)
            i.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            startActivityForResult(i, REQUEST_CODE_CAMERA)
        } catch (e: ActivityNotFoundException){
            Log.e(TAG, "openCameraForImage: $e")
            Toast.makeText(this, "Ваше приложение временно не поддерживать поиск через камеру!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri {
        var tempDir = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.absolutePath + "/.temp/")
        tempDir.mkdir()
        val tempFile = File.createTempFile(generateFileName(), ".jpg", tempDir)
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        //write the bytes in file

        //write the bytes in file
        val fos = FileOutputStream(tempFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return Uri.fromFile(tempFile)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK){
            //var bit: Bitmap = data?.extras?.get("data") as Bitmap
            if(data == null){
                val ftpClient = FTPClient()
                thread {
                    try{
                        dialogChoose.dismiss()
                        ftp.connect(SERVER_IP, SERVER_USERNAME, SERVER_PASSWORD)
                        //val fileName = generateFileName()
                        val file = File("storage/emulated/0/DCIM/Camera/$fileName.jpg")
                        ftp.upload("$SERVER_DIR/$fileName.jpg", file.toString(), this)
                        fileURL = "$fileName.jpg"
                        urlGet()
                        ftp.disconnect()

                        // api
                        val bitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
                        val uriCameraImage = bitmapToUri(bitmap).toString()
                        val cameraImage = uriCameraImage.substring(8, uriCameraImage.length)
                        Log.i(TAG, "onActivityResult: ${cameraImage}")
                        var base64ImageString = encoder(cameraImage)
                        base64ImageString = URLEncoder.encode(base64ImageString, "UTF-8")

                        var result: String?
                        try {
                            val params: HashMap<String, String> = HashMap()
                            params["img"] = base64ImageString
                            result = makeRequest(Helpers.link, Helpers.RequestMethod.POST, params)
                            Log.i(TAG, "json ответ: $result")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.i(TAG, "onActivityResult: $e")
                        }
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WebViewFragment()).commit()
                    } catch (e: Exception){
                        e.printStackTrace()
                        Log.e(TAG, "onActivityResult, thread: $e")
                    }
                }

            }
        }

        // ftp галерея
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK){
            thread {
                try {
                    dialogChoose.dismiss()
                    ftp.connect(SERVER_IP, SERVER_USERNAME, SERVER_PASSWORD)
                    val fileName = generateFileName()
                    val file = File(getPath(data?.data))
                    Log.i(TAG, "onActivityResult: $SERVER_DIR/${file.name}")
                    ftp.upload("$SERVER_DIR/$fileName.jpg", getPath(data?.data).toString(), this)
                    fileURL = "$fileName.jpg"
                    urlGet()
                    ftp.disconnect()

                    // api
                    var base64ImageString = encoder(getPath(data?.data).toString())
                    base64ImageString = URLEncoder.encode(base64ImageString, "UTF-8")

                    try {
                        val params: HashMap<String, String> = HashMap()
                        params["img"] = base64ImageString
                        result = makeRequest(Helpers.link, Helpers.RequestMethod.POST, params)
                        Log.i(TAG, "json ответ: $result")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.i(TAG, "onActivityResult: $e")
                    }

                    //photosUrlList.add("http112://$SERVER_IP/$fileName.jpg")
                    photosUrlList.add("https://ne-dieta.ru/wp-content/uploads/2017/11/final_1200-7.jpg")
                    Log.i(TAG, "onActivityResult: $SERVER_IP/$fileName.jpg")
                    saveData(photosUrlList)
                    Log.i(TAG, "photosUrlList size = : ${photosUrlList.size}")

                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WebViewFragment()).commit()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "onActivityResult photochoose: ", e)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveData(arrayOfPhotos: ArrayList<String>) {
        val sharedPref = getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonString = gson.toJson(arrayOfPhotos)
        val editor = sharedPref.edit()
        editor.putString("list", jsonString)
        editor.apply()
    }

    fun showSavedUrl() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        if (sharedPref.contains("list")) {
            val stringList = sharedPref.getString("list", "у тебя не работает нихуя осел")
            val gson = Gson()
            val type = object : TypeToken<java.util.ArrayList<String?>?>() {}.type
            photosUrlList = gson.fromJson(stringList, type) as ArrayList<String>
        }
    }

    fun getMyData(): String? {
        return result
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun encoder(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    // <- Генератор названия файла -> //
    private fun generateFileName() : String {
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("ddmmyyyy", Locale.getDefault())
        val timeFormat: DateFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val resultGenerator = (1..20).map { allowedChars.random() }.joinToString("")
        return dateFormat.format(currentDate) + "_" + timeFormat.format(currentDate) + "_" + resultGenerator
    }
    // <-                          -> //


    fun urlGet() : String{
        return fileURL
    }
    var infofragment = ""
    fun getInfoFragment() : String {
        return infofragment
    }
    fun openDialog(){
        dialogChoose.setContentView(R.layout.dialog)
        dialogChoose.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogChoose.show()
    }

    // <- Кнопки в меню -> //
    fun feedback(view: View) { openFragment(InfoFragment(), "feedback") }
    fun refund(view: View) { openFragment(InfoFragment(), "refund") }
    fun subscriptioncontrol(view: View) { openFragment(InfoFragment(), "subscription_control") }
    fun premiumsearch(view: View) { openFragment(PremiumFragment()) }
    fun mainsearch(view: View) { openFragment(MainFragment()) }
    // <-               -> //

    private fun openFragment(fragment: Fragment, str: String = ""){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        drawerLayout.closeDrawer(GravityCompat.START)
        infofragment = str
    }
    // <- Кнопки в диалоге -> //
    fun closeDialog(view: View) { dialogChoose.dismiss() }          // button for close dialog
    fun chooseImageDialog(view: View) { openGalleryForImage() }             // button for choose photo from the gallery
    fun photoCameraDialog(view: View) { openCameraForImage() }              // button for open camera

    override fun passDataCom(url: String) {
        val bundle = Bundle()
        bundle.putString("url", url)

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = UserWebViewFragment()
        frag2.arguments = bundle

        transaction.replace(R.id.fragment_container, frag2)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
    // <-                  -> //





    // залупа
//    if (resultCode == Activity.RESULT_OK && requestCode == 1){
//        val file = File(getPath(data?.data))
////            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
//        val myImage = File(getPath(data?.data))
//        val uriImage: Uri? = data?.data
//        var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
//
//        Log.i(TAG, "${data?.data} ")
//        Log.i(TAG, "Выбранный файл из галереи: $file")
//
//        val bytes = File(getPath(data?.data)).readBytes()
//        val base64 = Base64.getEncoder().encodeToString(bytes)
//        Log.i(TAG, "длина ебучей base64: ${base64.length}")
//
////            getCurrentData(base64)
//
//
////            thread {
////                try {
////                    val con: HttpURLConnection = URL("https://data.av100.ru/socialapi.ashx").openConnection() as HttpURLConnection
////
////                    con.requestMethod = RequestMethod.POST.toString()
////                    con.setRequestProperty("Content-Type", "multipart/form-data")
////                    con.doOutput = true
////
////                    val data = "key=e9b9368f-0f87-4bfd-baab-e40ba235ec85&method=byphoto&img=data:image/png;base64$base64"
////                    val wr = DataOutputStream(con.outputStream)
////                    wr.writeChars(data)
////                    wr.flush()
////
////                    val `in` = BufferedReader(InputStreamReader(con.inputStream))
////                    var inputLine: String?
////                    val response = StringBuffer()
////
////                    while (`in`.readLine().also { inputLine = it } != null) {
////                        response.append(inputLine)
////                    }
////                    Log.i(TAG, "response: $response")
////                } catch (e: Exception) {
////                    e.printStackTrace()
////                    Log.i(TAG, "с апи http прикол: $e")
////                }
////            }
//
//        val ftpClient = FTPClient()

}
//var bit: Bitmap = data?.extras?.get("data") as Bitmap
//
//var uriCameraImage = bitmapToUri(bit).toString()
//var cameraImage = uriCameraImage.substring(8, uriCameraImage.length)
//Log.i(TAG, "onActivityResult: ${cameraImage}")
//
//var base64ImageString = encoder(cameraImage)
//base64ImageString = URLEncoder.encode(base64ImageString, "UTF-8")
//
//doApiRequest(base64ImageString)


