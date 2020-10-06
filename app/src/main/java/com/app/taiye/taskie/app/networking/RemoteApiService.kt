package com.app.taiye.taskie.app.networking

import com.app.taiye.taskie.app.model.Task
import com.app.taiye.taskie.app.model.request.AddTaskRequest
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.model.response.CompleteNoteResponse
import com.app.taiye.taskie.app.model.response.GetTasksResponse
import com.app.taiye.taskie.app.model.response.RegisterResponse
import com.app.taiye.taskie.app.model.response.UserProfileResponse
import com.app.taiye.taskie.app.model.response.LoginResponse
import  retrofit2.Call
import retrofit2.http.*


/**
 * Api service to build Retrofit-powered API calls.
 */
interface RemoteApiService {

    @POST("/api/register")
    fun registerUser(@Body userDataRequest: UserDataRequest): Call<RegisterResponse>

    @GET("/api/note")
    fun getNotes(@Header("Authorization") token:String): Call<GetTasksResponse>


    @POST("/api/note")
    fun addTask(@Header("Authorization") token:String,@Body addTaskRequest: AddTaskRequest): Call<Task>

    @POST("/api/login")
    fun loginUser(@Body userDataRequest:UserDataRequest): Call<LoginResponse>

    @GET("/api/user/profile")
    fun getMyProfile(@Header("Authorization") token:String):Call<UserProfileResponse>

    @POST("api/note/complete")
    fun completeTask(@Header("Authorization") token:String, @Query("id") noteId: String): Call<CompleteNoteResponse>


}