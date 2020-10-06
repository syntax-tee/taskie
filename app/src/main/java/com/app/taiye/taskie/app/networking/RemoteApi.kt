package com.app.taiye.taskie.app.networking

import com.app.taiye.taskie.app.App
import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.UserProfile
import com.app.taiye.taskie.app.model.request.AddTaskRequest
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.model.response.CompleteNoteResponse
import com.app.taiye.taskie.app.model.response.GetTasksResponse
import com.app.taiye.taskie.app.model.response.UserProfileResponse
import com.google.gson.Gson
import com.raywenderlich.android.taskie.model.response.LoginResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {

    private val gson = Gson()

    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
        val body = RequestBody.create(
            MediaType.parse("application/json"), gson.toJson(userDataRequest)
        )

        apiService.loginUser(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val jsonBody = response.body()?.string()

                if (jsonBody == null) {
                    onUserLoggedIn(null, NullPointerException("No response body"))
                }

                val loginResponse = gson.fromJson(jsonBody, LoginResponse::class.java)

                if (loginResponse == null || loginResponse.token.isNullOrBlank()) {
                    onUserLoggedIn(loginResponse.token, NullPointerException("No response body"))
                } else {
                    if(loginResponse!= null){
                        onUserLoggedIn(loginResponse.token, null)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onUserLoggedIn(null, t)

            }

        })
    }


    fun registerUser(
        userDataRequest: UserDataRequest,
        onUserCreated: (String?, Throwable?) -> Unit
    ) {
        val body = RequestBody.create(
            MediaType.parse("application/json"), gson.toJson(userDataRequest)
        )

        apiService.registerUser(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val message = response.body()?.string()
                if (message == null) {
                    onUserCreated(null, NullPointerException("No response body"))
                }
                onUserCreated(message, null)
            }

            override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
                onUserCreated(null, error)

            }

        })
    }

    fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {
        apiService.getNotes(App.getToken()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val jsonBody = response.body()?.string()

                if (jsonBody == null) {
                    onTasksReceived(emptyList(), NullPointerException("No data available"))
                }

                val data = gson.fromJson(jsonBody, GetTasksResponse::class.java)
                if (data != null && !data.notes.isEmpty()) {
                    onTasksReceived(data.notes.filter {
                        !it.isCompleted
                    }, null)
                } else {
                    onTasksReceived(emptyList(), NullPointerException("No data available"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
                onTasksReceived(emptyList(), error)
            }

        })
    }

    fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
        onTaskDeleted(null)
    }

    fun completeTask(taskId: String,onTaskCompleted: (Throwable?) -> Unit) {
       apiService.completeTask(App.getToken(),taskId).enqueue(object: Callback<ResponseBody>{
           override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
               val jsonBody = response.body()?.string()

               if(jsonBody == null){
                   onTaskCompleted(java.lang.NullPointerException("No response"))
                 return
               }

               val completeNoteResponse = gson.fromJson(jsonBody, CompleteNoteResponse::class.java)
               if(completeNoteResponse?.message == null){
                   onTaskCompleted(NullPointerException("No response"))
               }else{
                   onTaskCompleted(null)
               }

           }

           override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
               onTaskCompleted(error)
           }

       })
    }

    fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {
        val body = RequestBody.create(
            MediaType.parse("application/json"), gson.toJson(addTaskRequest)
        )

        apiService.addTask(App.getToken(),body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val jsonBody = response.body()?.string()
                if (jsonBody == null) {
                    onTaskCreated(null, NullPointerException("No data available"))
                } else {
                    val getTaskResponse = gson.fromJson(jsonBody, Task::class.java)
                    if (getTaskResponse != null) {
                        onTaskCreated(
                            Task(
                                getTaskResponse.id,
                                getTaskResponse.title,
                                getTaskResponse.content,
                                getTaskResponse.isCompleted,
                                getTaskResponse.taskPriority
                            ),
                            null,
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
                onTaskCreated(null, error)
            }

        })
    }

    fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
        getTasks { tasks, error ->
            if (error != null && error !is java.lang.NullPointerException) {
                onUserProfileReceived(null, error)
                return@getTasks
            }

            apiService.getMyProfile(App.getToken()).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val jsonBody = response.body()?.string()
                    if (jsonBody == null) {
                        onUserProfileReceived(null, error)
                        return
                    }

                    val userProfileResponse =
                        gson.fromJson(jsonBody, UserProfileResponse::class.java)
                    if (userProfileResponse.email == null || userProfileResponse.name == null) {
                        onUserProfileReceived(null, error)
                    } else {
                        onUserProfileReceived(
                            UserProfile(
                                userProfileResponse.email,
                                userProfileResponse.name,
                                tasks.size
                            ), error
                        )

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    onUserProfileReceived(null, error)
                }

            })
        }
    }
}