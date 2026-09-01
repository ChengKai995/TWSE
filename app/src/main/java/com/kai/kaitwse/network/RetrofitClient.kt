package com.kai.kaitwse.network

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import com.kai.kaitwse.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TAG = "network"
    private const val BASE_URL = "https://openapi.twse.com.tw/"
    private val httpLoggingInterceptor = HttpLoggingInterceptor{ message ->
        Log.d(TAG, message)
    }.apply {
        level = if(BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val stockService: StockService by lazy {
        retrofit.create(StockService::class.java)
    }
}