package com.koshkatolik.photoboost.historylist

import com.koshkatolik.photoboost.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class HistoryAdapter(private val imagesUrl: ArrayList<Bitmap?>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView? = (view.findViewById<View>(R.id.history_image) as? ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return MyViewHolder(itemView)
    }
    // выводим всю хуйню ну там setText() и прочее
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = imagesUrl[position]

//        lateinit var bitmap: Bitmap
//        thread {
//            bitmap = getBitmapFromURL("https://uvelirnoedelo.ru/wp-content/uploads/2020/12/skarabej.jpg")!!
//        }
        holder.image?.setImageBitmap(bitmap)
        //holder.image?.setImageBitmap(getBitmapFromURL("https://uvelirnoedelo.ru/wp-content/uploads/2020/12/skarabej.jpg"))
//        holder.image?.setImageBitmap(getBitmapFromURL(imageUrl))
//        holder.textName.text = user.name
//        holder.image.setImageBitmap(user.bitmap)
//
//        holder.textOpenProfile.setOnClickListener {
//            onUserClickListener.onUserClickListener(position)
//        }
    }

    override fun getItemCount(): Int {
        return imagesUrl.size
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
            Log.i("eblan", "getBitmapFromURL: $e")
            return null
        }
    }

}