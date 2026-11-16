package com.homeofhope.app.data.repository

import com.homeofhope.app.data.model.Cause
import com.homeofhope.app.data.network.AuthApiService
import java.io.IOException

class CauseRepository(
    private val api: AuthApiService
) {
    suspend fun getCauses(): List<Cause> {
        val response = api.getCauses()
        val body = response.body()

        if (!response.isSuccessful || body?.success != true) {
            throw IOException(body?.message ?: "Unable to load causes.")
        }

        return body.causes
    }
}
