package com.example.photosearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class UsersAdapter(private val users: ArrayList<OneVkUser>, private val onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById<View>(R.id.vk_user_name) as TextView
        val image: ImageView = view.findViewById<View>(R.id.vk_user_image) as ImageView
        val textOpenProfile: TextView = view.findViewById<View>(R.id.vk_open_profile_text) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }
    // выводим всю хуйню ну там setText() и прочее
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]

        holder.textName.text = user.name
        if (user.bannedStatus)
            holder.image.setImageResource(R.drawable.banned)
        else
            holder.image.setImageBitmap(user.bitmap)

        holder.textOpenProfile.setOnClickListener {
            onUserClickListener.onUserClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

}