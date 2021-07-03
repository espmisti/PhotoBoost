package com.example.photosearch

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import org.apache.commons.net.ftp.FTPClient
import java.io.*
import java.io.File
import kotlin.concurrent.thread

private const val REQUEST_CODE = 42
class MainActivity : AppCompatActivity(){

    val TAG = "saske"
    lateinit var dialogChoose: Dialog

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            Toast.makeText(this, "$takenImage", Toast.LENGTH_SHORT).show()
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val file = File(getPath(data?.data))
            val uriImage: Uri? = data?.data
            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)

            Log.i(TAG, "${data?.data} ")
            Log.i(TAG, "Выбранный файл из галереи: $file")

            val ftpClient = FTPClient()
            thread {
                try {
                    ftpClient.connect("z96082yn.beget.tech")
                    ftpClient.login("z96082yn", "XTYSDbPI")
                    ftpClient.enterLocalPassiveMode()
                    Log.i(TAG, "CONNECTED")
                    val dirPath = "./z96082yn.beget.tech/public_html"

//                    var filea: File = File("/storage/emulated/0/DCIM/fg/resizeq.jpg")
//                    val os: OutputStream = BufferedOutputStream(FileOutputStream(filea))
////                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 400, false)
////                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
//                    os.close();
//
//                    val file = myImage
//                    Log.i(TAG, "file: $filea")
//
//                    val inputStream: InputStream = FileInputStream("/storage/emulated/0/DCIM/fg/resizeq.jpg")

//                    var filea: File = File(myImage.path)

//                    val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, false)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
//                    os.close()


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
    fun toPhotograph(){
        var photo = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(photo.resolveActivity(this.packageManager) != null){
            startActivityForResult(photo, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Негры", Toast.LENGTH_SHORT).show()
        }
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

    fun openDialog(){
        dialogChoose.setContentView(R.layout.dialog)
        dialogChoose.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogChoose.show()
    }



    // <- Кнопки в диалоге -> //

    fun closeDialog(view: View) { dialogChoose.dismiss() }          // button for close dialog
    fun chooseImageDialog(view: View) { insertPhoto() }             // button for choose photo from the gallery
    fun photoCameraDialog(view: View) { toPhotograph() }            // button for open camera

    // <-                  -> //


}
