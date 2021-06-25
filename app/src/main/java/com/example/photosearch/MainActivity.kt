package com.example.photosearch
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        var selectedFragment: Fragment? = null
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

}
