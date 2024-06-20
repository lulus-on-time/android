package com.lulusontime.findmyself.map.data.retrofit

import com.lulusontime.findmyself.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getFloorApiConfig(): ApiService {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY })
                .build()

            val retrofit = Retrofit.Builder().client(client).baseUrl(BuildConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            return retrofit.create(ApiService::class.java)


        }
    }
}