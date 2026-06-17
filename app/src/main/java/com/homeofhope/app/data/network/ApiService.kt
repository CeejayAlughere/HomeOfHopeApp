package com.homeofhope.app.data.network

import com.homeofhope.app.data.model.ApiResponse
import com.homeofhope.app.data.model.ContactMessage
import com.homeofhope.app.data.model.NewsletterSubscriber
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Contact
    @POST("contact")
    suspend fun submitContact(@Body contact: ContactMessage): ApiResponse

    // Newsletter
    @POST("newsletter/subscribe")
    suspend fun subscribeNewsletter(@Body body: NewsletterSubscriber): ApiResponse
}
