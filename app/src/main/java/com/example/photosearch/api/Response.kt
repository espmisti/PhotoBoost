package com.example.photosearch.api

data class Response(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Int,
    val is_closed: Boolean,
    val last_name: String,
    val photo_200: String,
    val deactivated: String
)