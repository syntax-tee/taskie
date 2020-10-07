package com.app.taiye.taskie.app.networking

import com.app.taiye.taskie.app.model.Task
import com.app.taiye.taskie.app.model.request.AddTaskRequest
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.model.response.*
import  retrofit2.Call
import retrofit2.http.*


/**
 * Api service to build Retrofit-powered API calls.
 */
interface RemoteApiService {

    @POST("/api/register")
    fun registerUser(@Body userDataRequest: UserDataRequest): Call<RegisterResponse>

    @GET("/api/note")
    fun getNotes(): Call<GetTasksResponse>


    @POST("/api/note")
    fun addTask(@Body addTaskRequest: AddTaskRequest): Call<Task>

    @POST("/api/login")
    fun loginUser(@Body userDataRequest:UserDataRequest): Call<LoginResponse>

    @GET("/api/user/profile")
    fun getMyProfile():Call<UserProfileResponse>

    @POST("api/note/complete")
    fun completeTask(@Query("id") noteId: String): Call<CompleteNoteResponse>


    @DELETE("api/note/")
    fun deleteNote(@Query("id") note_id: String): Call<DeleteNoteResponse>

}