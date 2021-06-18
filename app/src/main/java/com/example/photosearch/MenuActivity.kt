package com.example.photosearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.statusBarColor = ContextCompat.getColor(this,R.color.black);
        (findViewById<ImageView>(R.id.b_menu_back)).setOnClickListener{ finish() }
        (findViewById<LinearLayout>(R.id.m_button_share)).setOnClickListener{
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "PhotoSearch")
            i.putExtra(Intent.EXTRA_TEXT, "Попробуй это приложение для поиска по фото\n\nhttps://vk.com/putin")
            startActivity(Intent.createChooser(i, "choose one"))
        }
    }
}