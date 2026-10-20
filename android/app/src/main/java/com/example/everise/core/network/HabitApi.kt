package com.example.everise.core.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HabitApi {
    @GET("habits")
    suspend fun list(
        @Header("Authorization") bearer: String,
        @Query("archived") archived: Boolean = false,
    ): ApiResponse<List<RemoteHabit>>

    @POST("habits")
    suspend fun create(
        @Header("Authorization") bearer: String,
        @Body body: RemoteHabit,
    ): ApiResponse<RemoteHabit>

    @PATCH("habits/{id}")
    suspend fun update(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body body: RemoteHabit,
    ): ApiResponse<RemoteHabit>

    @POST("habits/{id}/archive")
    suspend fun archive(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): ApiResponse<RemoteHabit>

    @DELETE("habits/{id}")
    suspend fun delete(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): ApiResponse<Map<String, String>>

    @POST("habit-logs")
    suspend fun createLog(
        @Header("Authorization") bearer: String,
        @Body body: CreateHabitLogRequest,
    ): ApiResponse<RemoteHabitLog>

    @GET("habit-logs")
    suspend fun listLogs(
        @Header("Authorization") bearer: String,
        @Query("habitId") habitId: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
    ): ApiResponse<List<RemoteHabitLog>>
}
