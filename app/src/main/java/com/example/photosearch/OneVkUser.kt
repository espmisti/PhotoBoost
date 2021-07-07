package com.example.photosearch

import android.graphics.Bitmap

data class OneVkUser(
    val bitmap: Bitmap?,
    val bannedStatus: Boolean,
    val name: String,
    val userUrl: String,
)

