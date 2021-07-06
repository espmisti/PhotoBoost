package com.example.photosearch

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(){

    val TAG = "saske"
    lateinit var dialogChoose: Dialog

    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_CODE_CAMERA = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        navigationView2.itemIconTintList = null

        // Подгрузка основного фрагмента
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
        dialogChoose=Dialog(this)
        btnSearch.setOnClickListener {
// openGalleryForImage()
            openDialog()
// var intent = Intent(this, PhotoWebViewActivity::class.java)
// startActivity(intent)
        }
        var selectedFragment: Fragment? = null
//textDescMain.text = Html.fromHtml("<u>Подписка активна<br></u>")
        bottonNavigatorView.background = null
        floatbar_bg.isEnabled = false






// btnChoosePlan.setOnClickListener{
// startActivity(Intent(this, PhotoWebViewActivity::class.java))
// }

// btnHistory.setOnClickListener {
// startActivity(Intent(this, HistoryActivity::class.java))
// finish()
// }
        bottonNavigatorView.setOnNavigationItemSelectedListener{
            item -> when (item.itemId){
                R.id.mPremium ->{
                    selectedFragment = PremiumFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        selectedFragment!!
                    ).commit()
                }
                R.id.mMenu -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

    // взять фотку из галереи
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }
    val CAMERA_REQUEST = 64
    private fun photograph(){
        try{
            val i: Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, CAMERA_REQUEST)
        } catch (e: ActivityNotFoundException){
            Log.e(TAG, "photograph: $e")
            Toast.makeText(this, "Ваше приложение временно не поддерживать поиск через камеру!", Toast.LENGTH_SHORT).show()
        }

    }
//    private fun generateFileUri(type: Int): Uri? {
//        var file: File? = null
//        when (type) {
//            TYPE_PHOTO -> file = File(
//                directory.getPath().toString() + "/" + "photo_"
//                        + System.currentTimeMillis() + ".jpg"
//            )
//            TYPE_VIDEO -> file = File(
//                (directory.getPath().toString() + "/" + "video_"
//                        + System.currentTimeMillis() + ".mp4")
//            )
//        }
//        Log.d(TAG, "fileName = $file")
//        return Uri.fromFile(file)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            //val myImage = File(getPath(data?.data))
            var bit: Bitmap = data?.extras?.get("data") as Bitmap
            val ftpClient = FTPClient()
            thread {
                try{
                    connectFTP(ftpClient)
                    Log.i(TAG, "CONNECTED")
                    val dirPath = "./www/tanya.ru"

                    val filea: File = File("/storage/emulated/0/DCIM/fg/photo.jpg")
                    val os: OutputStream = BufferedOutputStream(FileOutputStream(filea))
                    //bit = Bitmap.createScaledBitmap(bit, 800, 1000, false)
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close()
                    val inputStream: InputStream = FileInputStream("/storage/emulated/0/DCIM/fg/photo.jpg")
                    Log.i(TAG, "file: $dirPath/${filea.name}")
                    ftpClient.storeFile("$dirPath/${filea.name}", inputStream)
                    inputStream.close()

                    ftpClient.logout()
                    ftpClient.disconnect()
                    Log.i(TAG, "DISCONNECTED")


                } catch (e: java.lang.Exception){
                    e.printStackTrace()
                    Log.e(TAG, "onActivityResult, thread: $e", )
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val myImage = File(getPath(data?.data))
            val uriImage: Uri? = data?.data
            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)

            Log.i(TAG, "${data?.data} ")
            Log.i(TAG, "Выбранный файл из галереи: $myImage")

            val ftpClient = FTPClient()
            thread {
                try {
                    connectFTP(ftpClient)
                    Log.i(TAG, "CONNECTED")
                    val dirPath = "./www/tanya.ru"

                    var filea: File = File("/storage/emulated/0/DCIM/fg/resizeq.jpg")
                    val os: OutputStream = BufferedOutputStream(FileOutputStream(filea))
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 600, false)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close();

                    val file = myImage
                    Log.i(TAG, "file: $filea")


                    val inputStream: InputStream = FileInputStream(file)

                    Log.i(TAG, "twetwetwet: $$dirPath/${file.name}")
                    ftpClient.storeFile("$dirPath/${file.name}", inputStream)
                    inputStream.close()
//END OF FILE UPLOADING
                    ftpClient.logout()
                    ftpClient.disconnect()
                    Log.i(TAG, "DISCONNECTED")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(TAG, "ты тупой еблан $e")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun insertPhoto() {
        val hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
            return
        }
        openGalleryForImage()
    }
//    private val MY_CAMERA_PERMISSION_CODE = 100
////    fun toPhotograph(){
////        val hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
////        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
////            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_CAMERA_PERMISSION_CODE)
////            return
////        }
////        openGalleryForImage()
////
////        var photo = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////        if(photo.resolveActivity(this.packageManager) != null){
////            startActivityForResult(photo, REQUEST_CODE_ASK_PERMISSIONS)
////        } else {
////            Toast.makeText(this, "Негры", Toast.LENGTH_SHORT).show()
////        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
// Permission Granted
                openGalleryForImage()
            } else {
// Permission Denied
                Toast.makeText(this@MainActivity, "нету разрешения очкоблядун блять", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun connectFTP(ftpClient: FTPClient){
        ftpClient.connect("91.236.136.123")
        ftpClient.login("u724370", "3H0j9U2s")
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
        ftpClient.enterLocalPassiveMode()
    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
//create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

//Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

//write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
    // Меню кнопки

    fun feedback(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FeedBackFragment()).commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun refund(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, RefundFragment()).commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun subscriptioncontrol(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SubscriptionControlFragment()).commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun premiumsearch(view: View) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PremiumFragment()).commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun subscription(view: View) {
// supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PremiumFragment()).commit()
// drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun openDialog(){
        dialogChoose.setContentView(R.layout.dialog)
        dialogChoose.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogChoose.show()
    }



    // <- Кнопки в диалоге -> //

    fun closeDialog(view: View) { dialogChoose.dismiss() }          // button for close dialog
    fun chooseImageDialog(view: View) { insertPhoto() }             // button for choose photo from the gallery
    fun photoCameraDialog(view: View) { photograph() }            // button for open camera

    // <-                  -> //


}
