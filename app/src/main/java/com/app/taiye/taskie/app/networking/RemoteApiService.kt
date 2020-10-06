package com.app.taiye.taskie.app.networking

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import  retrofit2.Call


/**
 * Api service to build Retrofit-powered API calls.
 */
interface RemoteApiService {

    @POST("/api/register")
    fun registerUser(@Body requestBody: RequestBody): Call<ResponseBody>

}