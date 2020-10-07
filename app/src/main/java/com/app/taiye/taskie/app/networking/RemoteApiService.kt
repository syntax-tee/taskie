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
   suspend fun registerUser(@Body userDataRequest: UserDataRequest): RegisterResponse

    @GET("/api/note")
    suspend fun getNotes(): GetTasksResponse


    @POST("/api/note")
    suspend fun addTask(@Body addTaskRequest: AddTaskRequest): Task

    @POST("/api/login")
    suspend fun loginUser(@Body userDataRequest:UserDataRequest): LoginResponse

    @GET("/api/user/profile")
    suspend fun getMyProfile():UserProfileResponse

    @POST("api/note/complete")
    suspend fun completeTask(@Query("id") noteId: String): CompleteNoteResponse


    @DELETE("api/note/")
    suspend  fun deleteNote(@Query("id") note_id: String): DeleteNoteResponse

}