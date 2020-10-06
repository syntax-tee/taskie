package com.app.taiye.taskie.app.networking

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import  retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


/**
 * Api service to build Retrofit-powered API calls.
 */
interface RemoteApiService {

    @POST("/api/register")
    fun registerUser(@Body requestBody: RequestBody): Call<ResponseBody>

    @GET("/api/note")
    fun getNotes(@Header("Authorization") token:String): Call<ResponseBody>


    @POST("/api/note")
    fun addTask(@Header("Authorization") token:String,@Body requestBody: RequestBody): Call<ResponseBody>

    @POST("/api/login")
    fun loginUser(@Body requestBody: RequestBody): Call<ResponseBody>

    @GET("/api/user/profile")
    fun getMyProfile(@Header("Authorization") token:String):Call<ResponseBody>


}