package com.example.flickerbrowser

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface APIInterface {

    @GET
    fun getphoto(@Url url: String?): Call<Photos?>?
}