package com.app.taiye.taskie.app.networking

import com.app.taiye.taskie.app.App
import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.UserProfile
import com.app.taiye.taskie.app.model.request.AddTaskRequest
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.model.response.GetTasksResponse
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {

    private val gson = Gson()

    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
        Thread(Runnable {
            val connection = URL("$BASE_URL/api/login").openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.readTimeout = 10000
            connection.connectTimeout = 100000
            connection.doOutput = true
            connection.doInput = true

            val body = gson.toJson(userDataRequest)

            val bytes = body.toByteArray()

            try {
                connection.outputStream.use { outputStream ->
                    outputStream.write(bytes)
                }

                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.useLines { lines ->
                        lines.forEach {
                            response.append(it.trim())
                        }
                    }

                    val jsonObject = JSONObject(response.toString())
                    val token = jsonObject.getString("token")
                    onUserLoggedIn(token, null)
                }
            } catch (error: Throwable) {
                onUserLoggedIn(null, error)
            }
            connection.disconnect()
        }).start()
        onUserLoggedIn("Login Successfully!", null)
    }


    fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
        val body = RequestBody.create(
            MediaType.parse("application/json"), gson.toJson(userDataRequest)
        )

        apiService.registerUser(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val message = response.body()?.string()
                if(message == null){
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
        Thread(Runnable {
            val connection = URL("$BASE_URL/api/note").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Authorization", App.getToken())
            connection.readTimeout = 10000
            connection.connectTimeout = 100000
            connection.doOutput = true
            connection.doOutput = true


            try {
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)

                    bufferedReader.useLines { lines ->
                        lines.forEach {
                            response.append(it.trim())
                        }
                    }
                    val tasksResponse = gson.fromJson(response.toString(), GetTasksResponse::class.java)
                    onTasksReceived(tasksResponse.notes ?: listOf(), null)
                }
            } catch (error: Throwable) {
                onTasksReceived(emptyList(), error)
            }
            connection.disconnect()
        }).start()
    }

    fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
        onTaskDeleted(null)
    }

    fun completeTask(onTaskCompleted: (Throwable?) -> Unit) {
        onTaskCompleted(null)
    }

    fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {

        Thread(Runnable {
            val connection = URL("$BASE_URL/api/note").openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Authorization", App.getToken())
            connection.readTimeout = 10000
            connection.connectTimeout = 100000
            connection.doOutput = true
            connection.doInput = true

            val body = gson.toJson(addTaskRequest)

            val bytes = body.toByteArray()

            try {
                connection.outputStream.use { outputStream ->
                    outputStream.write(bytes)
                }

                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.useLines { lines ->
                        lines.forEach {
                            response.append(it.trim())
                        }
                    }
                    val jsonObject = JSONObject(response.toString())


                    val task = Task(
                        jsonObject.getString("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getBoolean("isCompleted"),
                        jsonObject.getInt("taskPriority")
                    )

                    onTaskCreated(task, null)
                }
            } catch (error: Throwable) {
                onTaskCreated(null, error)
            }
            connection.disconnect()
        }).start()
    }

    fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
        onUserProfileReceived(UserProfile("mail@mail.com", "Filip", 10), null)
    }
}