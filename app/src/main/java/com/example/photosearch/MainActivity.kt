package com.example.photosearch
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Подгрузка основного фрагмента
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()

        btnSearch.setOnClickListener {
            openGalleryForImage()

        }
        var selectedFragment: Fragment? = null
        //textDescMain.text = Html.fromHtml("<u>Подписка активна<br></u>")
        bottonNavigatorView.background = null
        floatbar_bg.isEnabled = false


//        window.navigationBarColor = resources.getColor(R.color.black)
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.black);
//        window.statusBarColor = ContextCompat.getColor(this,R.color.black);

//        btnChoosePlan.setOnClickListener{
//            startActivity(Intent(this, PhotoWebViewActivity::class.java))
//        }

//        btnHistory.setOnClickListener {
//            startActivity(Intent(this, HistoryActivity::class.java))
//            finish()
//        }
        //(findViewById<ImageButton>(R.id.b_history)).setOnClickListener { startActivity(Intent(this, ))}
        bottonNavigatorView.setOnNavigationItemSelectedListener{
            item -> when (item.itemId){
                R.id.mPremium ->{
                    selectedFragment = PremiumFragment()
                }
                R.id.mMenu -> {
                    Toast.makeText(this, "ебанный даун", Toast.LENGTH_SHORT).show()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                selectedFragment!!
            ).commit()
            true
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            //imageView5.setImageURI(data?.data) // handle chosen image
            Toast.makeText(this, "${data?.data}", Toast.LENGTH_SHORT).show()
            //var intent = Intent(this, PhotoWebViewActivity::class.java)
            intent.putExtra("uri", data?.data.toString())
            startActivity(intent)
        }
    }
}
