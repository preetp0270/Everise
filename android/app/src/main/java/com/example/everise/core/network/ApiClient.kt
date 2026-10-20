package com.example.everise.core.network

import com.example.everise.BuildConfig
import com.example.everise.core.datastore.SessionStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(private val sessionStore: SessionStore) {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .addInterceptor(Interceptor { chain ->
            val req = chain.request()
            chain.proceed(req)
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val habitApi: HabitApi = retrofit.create(HabitApi::class.java)
}
