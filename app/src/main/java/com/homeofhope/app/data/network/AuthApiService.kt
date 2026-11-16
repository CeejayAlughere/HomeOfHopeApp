package com.homeofhope.app.data.network

import com.homeofhope.app.data.model.AuthResponse
import com.homeofhope.app.data.model.CauseDonationRequest
import com.homeofhope.app.data.model.CauseDonationResponse
import com.homeofhope.app.data.model.CausesResponse
import com.homeofhope.app.data.model.LoginRequest
import com.homeofhope.app.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("causes.php")
    suspend fun getCauses(): Response<CausesResponse>

    @POST("donate.php")
    suspend fun submitCauseDonation(
        @Header("Authorization") authorization: String,
        @Body body: CauseDonationRequest
    ): Response<CauseDonationResponse>
}
