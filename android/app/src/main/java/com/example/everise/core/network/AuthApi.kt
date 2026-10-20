package com.example.everise.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): ApiResponse<AuthPayload>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<AuthPayload>

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): ApiResponse<AuthPayload>

    @POST("auth/logout")
    suspend fun logout(@Body body: Map<String, String>)

    @GET("auth/me")
    suspend fun me(@Header("Authorization") bearer: String): ApiResponse<Map<String, RemoteUser>>
}
