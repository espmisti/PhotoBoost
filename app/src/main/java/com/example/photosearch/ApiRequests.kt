package com.example.photosearch

import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiRequests {
    @Headers("Content-type:multipart/form-data")
    @POST("/socialapi.ashx?key=e9b9368f-0f87-4bfd-baab-e40ba235ec85&method=byvkprofile")
    fun getUsers(@Query("img") imageBase64: String?): Call<User>

    @Multipart
    @POST("/socialapi.ashx?key=e9b9368f-0f87-4bfd-baab-e40ba235ec85&method=byphoto")
    suspend fun uploadEmployeeData(@Query("img") map: RequestBody?): Call<User>
}