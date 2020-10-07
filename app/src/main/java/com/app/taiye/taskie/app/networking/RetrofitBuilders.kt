package com.app.taiye.taskie.app.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor

val contentType ="application/json".toMediaType()



fun buildRetrofit(): Retrofit {

    return  Retrofit.Builder()
        .client(buildClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
}

fun buildApiService(): RemoteApiService = buildRetrofit().create(RemoteApiService::class.java)


fun buildClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
   }).build()
