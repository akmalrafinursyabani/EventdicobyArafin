package com.example.eventdicobyarafin.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object ApiConfig {
    private const val BASE_API_URL = "https://event-api.dicoding.dev/"

    fun getApiService() : ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_API_URL).addConverterFactory(
            MoshiConverterFactory.create(moshi)).client(client).build()

        return retrofit.create(ApiService::class.java)
    }

}