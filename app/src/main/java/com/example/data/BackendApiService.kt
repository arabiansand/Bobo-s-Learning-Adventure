package com.example.data

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Header

interface BackendApiService {
    @GET("v2/me")
    suspend fun validatePiToken(
        @Header("Authorization") authorization: String
    ): PiUserResponse
}

@JsonClass(generateAdapter = true)
data class PiUserResponse(
    val username: String?
)
