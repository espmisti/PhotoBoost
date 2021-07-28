package com.example.photosearch.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {

    @GET("/method/users.get?user_ids=272531874&fields=photo_200&access_token=b1d877d67aaf7419600bb534a98573e1b164a3ccd75afddc2780aad91c09ab41a444031d7ee64a0a44455&v=5.131")
    fun getUsers(@Query("user_ids") userId: String): Call<VkUser>
}