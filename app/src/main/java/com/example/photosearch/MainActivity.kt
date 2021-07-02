package com.example.photosearch

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import me.echodev.resizer.Resizer
import org.apache.commons.net.ftp.FTPClient
import java.io.*
import java.io.File
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(){

    val TAG = "saske"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        navigationView2.itemIconTintList = null

        // Подгрузка основного фрагмента
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()


        btnSearch.setOnClickListener {
// openGalleryForImage()
            insertPhoto()
// var intent = Intent(this, PhotoWebViewActivity::class.java)
// startActivity(intent)
        }
        var selectedFragment: Fragment? = null
//textDescMain.text = Html.fromHtml("<u>Подписка активна<br></u>")
        bottonNavigatorView.background = null
        floatbar_bg.isEnabled = false


// window.navigationBarColor = resources.getColor(R.color.black)
// window.navigationBarColor = ContextCompat.getColor(this, R.color.black);
// window.statusBarColor = ContextCompat.getColor(this,R.color.black);

// btnChoosePlan.setOnClickListener{
// startActivity(Intent(this, PhotoWebViewActivity::class.java))
// }

// btnHistory.setOnClickListener {
// startActivity(Intent(this, HistoryActivity::class.java))
// finish()
// }
//(findViewById<ImageButton>(R.id.b_history)).setOnClickListener { startActivity(Intent(this, ))}
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val myImage = File(getPath(data?.data))
            val uriImage: Uri? = data?.data
            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)

            Log.i(TAG, "${data?.data} ")
            Log.i(TAG, "Выбранный файл из галереи: $myImage")

            val ftpClient = FTPClient()
            thread {
                try {
                    ftpClient.connect("z96082yn.beget.tech")
                    ftpClient.login("z96082yn", "XTYSDbPI")
                    ftpClient.enterLocalPassiveMode()
                    Log.i(TAG, "CONNECTED")
                    val dirPath = "./z96082yn.beget.tech/public_html"
//
// val file = File(R.drawable.bg_first)

                    var filea: File = File("/storage/emulated/0/DCIM/fg/resizeq.jpg")
                    val os: OutputStream = BufferedOutputStream(FileOutputStream(filea))
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 400, false)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close();

                    val file = myImage
                    Log.i(TAG, "file: $filea")

                    val inputStream: InputStream = FileInputStream("/storage/emulated/0/DCIM/fg/resizeq.jpg")


                    Log.i(TAG, "twetwetwet: $$dirPath/${filea.name}")
                    ftpClient.storeFile("$dirPath/${filea.name}", inputStream)
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


}