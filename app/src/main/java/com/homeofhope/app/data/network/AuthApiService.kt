package com.homeofhope.app.data.network

import com.homeofhope.app.data.model.AuthResponse
import com.homeofhope.app.data.model.LoginRequest
import com.homeofhope.app.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("login.php")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("register.php")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("logout.php")
    suspend fun logout(
        @Header("Authorization") authorization: String
    ): Response<AuthResponse>
}
