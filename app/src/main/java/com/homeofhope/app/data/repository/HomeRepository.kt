package com.homeofhope.app.data.repository

import com.homeofhope.app.data.model.ApiResponse
import com.homeofhope.app.data.model.ContactMessage
import com.homeofhope.app.data.model.Donation
import com.homeofhope.app.data.model.NewsletterSubscriber
import com.homeofhope.app.data.model.Volunteer
import com.homeofhope.app.data.network.ApiService

class HomeRepository(private val apiService: ApiService) {
    suspend fun submitDonation(donation: Donation): ApiResponse {
        return apiService.submitDonation(donation)
    }

    suspend fun submitVolunteer(volunteer: Volunteer): ApiResponse {
        return apiService.submitVolunteer(volunteer)
    }

    suspend fun submitContact(contact: ContactMessage): ApiResponse {
        return apiService.submitContact(contact)
    }

    suspend fun subscribeNewsletter(subscriber: NewsletterSubscriber): ApiResponse {
        return apiService.subscribeNewsletter(subscriber)
    }
}
