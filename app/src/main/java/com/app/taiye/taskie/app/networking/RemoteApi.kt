package com.app.taiye.taskie.app.networking

import com.app.taiye.taskie.app.App
import com.app.taiye.taskie.app.model.Failure
import com.app.taiye.taskie.app.model.Success
import com.app.taiye.taskie.app.model.Result
import com.app.taiye.taskie.app.model.Task
import com.app.taiye.taskie.app.model.UserProfile
import com.app.taiye.taskie.app.model.request.AddTaskRequest
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.model.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {


    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (Result<String>) -> Unit) {


        apiService.loginUser(userDataRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if (loginResponse == null || loginResponse.token.isNullOrBlank()) {
                    onUserLoggedIn(Failure(NullPointerException("No response body")))
                } else {
                    if(loginResponse!= null){
                        onUserLoggedIn(Success(loginResponse.token))
                    }
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onUserLoggedIn(Failure( t))

            }

        })
    }


    fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (Result<String>) -> Unit) {

        apiService.registerUser(userDataRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val message = response.body()
                if (message == null) {
                    onUserCreated(Failure( NullPointerException("No response body")))
                }
                onUserCreated(Success(response.message()))
            }

            override fun onFailure(call: Call<RegisterResponse>, error: Throwable) {
                onUserCreated(Failure( error))
            }

        })
    }

    fun getTasks(onTasksReceived: (Result<List<Task>>) -> Unit) {
        apiService.getNotes(App.getToken()).enqueue(object : Callback<GetTasksResponse> {
            override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {

                val data = response.body()
                if (data != null && data.notes.isNotEmpty()) {
                    onTasksReceived(Success(data.notes.filter {
                        !it.isCompleted
                    }))
                } else {
                    onTasksReceived(Failure(NullPointerException("No data available")))
                }
            }
            override fun onFailure(call: Call<GetTasksResponse>, error: Throwable) {
                onTasksReceived(Failure(error))
            }

        })
    }

    fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
        onTaskDeleted(null)
    }

    fun completeTask(taskId: String,onTaskCompleted: (Result<String>) -> Unit) {
       apiService.completeTask(App.getToken(),taskId).enqueue(object: Callback<CompleteNoteResponse>{
           override fun onResponse(
               call: Call<CompleteNoteResponse>,
               response: Response<CompleteNoteResponse>
           ) {
               val completeNoteResponse = response.body()

               if(completeNoteResponse == null){
                   onTaskCompleted(Failure(NullPointerException("No response")))
                   return
               }

               if(completeNoteResponse?.message == null){
                   onTaskCompleted(Failure(NullPointerException("No response")))
               }else{
                   onTaskCompleted(Failure(NullPointerException("No response")))
               }
           }
           override fun onFailure(call: Call<CompleteNoteResponse>, error: Throwable) {
               onTaskCompleted(Failure(error))
           }


       })
    }

    fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Result<Task>) -> Unit) {


        apiService.addTask(App.getToken(),addTaskRequest).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                val getTaskResponse = response.body()
                if (getTaskResponse == null) {
                    onTaskCreated(Failure(NullPointerException("No data available")))
                } else {
                    onTaskCreated(Success(   Task(
                        getTaskResponse.id,
                        getTaskResponse.title,
                        getTaskResponse.content,
                        getTaskResponse.isCompleted,
                        getTaskResponse.taskPriority
                    )))
                }
            }

            override fun onFailure(call: Call<Task>, error: Throwable) {
                onTaskCreated(Failure(error))
            }

        })
    }

    fun getUserProfile(onUserProfileReceived: (Result<UserProfile>) -> Unit) {
        getTasks { result ->
            if(result is Failure && result.error !is java.lang.NullPointerException) {
                onUserProfileReceived(Failure(result.error))
                return@getTasks
            }
            val tasks = result as Success

            apiService.getMyProfile(App.getToken()).enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {

                    val userProfileResponse = response.body()

                    if (userProfileResponse?.email == null || userProfileResponse.name == null) {
                        onUserProfileReceived(Failure(NullPointerException("User response is null")))
                    } else {
                        onUserProfileReceived(Success(UserProfile(
                                userProfileResponse.email,
                                userProfileResponse.name,
                                tasks.data.size
                            ))
                        )
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    onUserProfileReceived(Failure(Exception("No response from server")))
                }

            })
        }
    }
}