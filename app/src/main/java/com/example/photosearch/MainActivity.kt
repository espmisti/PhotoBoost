package com.example.photosearch
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        window.navigationBarColor = resources.getColor(R.color.black)
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.black);
//        window.statusBarColor = ContextCompat.getColor(this,R.color.black);

        //(findViewById<ImageButton>(R.id.b_history)).setOnClickListener { startActivity(Intent(this, ))}
    }

}
