package com.app.taiye.taskie.app.networking

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


    suspend fun loginUser(userDataRequest: UserDataRequest): Result<String> = try {
        val result = apiService.loginUser(userDataRequest)
        Success(result.token)
    } catch (error: Throwable) {
        Failure(error)
    }


    suspend fun registerUser(userDataRequest: UserDataRequest): Result<String> = try {
        val result = apiService.registerUser(userDataRequest)
        Success(result.message)
    } catch (error: Throwable) {
        Failure(error)
    }

    suspend fun getTasks(): Result<List<Task>> = try {
        val data = apiService.getNotes()
        if (data.notes.isNotEmpty()) {
            Success(data.notes.filter { !it.isCompleted })
        } else {
            Failure(NullPointerException("No data available"))
        }
    } catch (error: Throwable) {
        Failure(error)
    }


    suspend fun deleteTask(taskId: String): Result<String> = try {
        val data = apiService.deleteNote(taskId)
        Success(data.message)
    } catch (error: Throwable) {
        Failure(error)
    }


    suspend fun completeTask(taskId: String): Result<String> = try {
        val response = apiService.completeTask(taskId)
        Success(response.message!!)
    } catch (error: Throwable) {
        Failure(error)
    }


    suspend fun addTask(addTaskRequest: AddTaskRequest): Result<Task> = try {
        val getTaskResponse = apiService.addTask(addTaskRequest)
        Success(getTaskResponse)
    } catch (error: Throwable) {
        Failure(error)
    }

    suspend fun getUserProfile(): Result<UserProfile> = try {
        val noteResult = getTasks()
        if (noteResult is Failure) {
            Failure(noteResult.error)
        } else {
            val notes = noteResult as Success
            val data = apiService.getMyProfile()
            if (data.email == null || data.name == null) {
                Failure(NullPointerException("No data available!"))
            } else {
                Success(UserProfile(data.email, data.name, notes.data.size))
            }
        }
    } catch (error: Throwable) {
        Failure(error)
    }
}