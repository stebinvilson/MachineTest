package com.restoo.machinetest.Listner

import com.restoo.machinetest.Apod
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Accept: application/json", "Content-Type: application/json;charset=UTF-8")
    @GET("planetary/apod/")
    fun fetch(@Query("api_key") api_key: String, @Query("count")count : Int): Call<List<Apod>?>
}