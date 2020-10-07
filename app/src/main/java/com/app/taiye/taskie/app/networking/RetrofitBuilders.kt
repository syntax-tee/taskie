package com.app.taiye.taskie.app.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

val contentType ="application/json".toMediaType()

fun buildClient(): OkHttpClient = OkHttpClient.Builder().build()


fun buildRetrofit(): Retrofit {

    return  Retrofit.Builder()
        .client(buildClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
}

fun buildApiService(): RemoteApiService = buildRetrofit().create(RemoteApiService::class.java)