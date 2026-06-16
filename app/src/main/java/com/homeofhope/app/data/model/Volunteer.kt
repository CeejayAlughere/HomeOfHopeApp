package com.homeofhope.app.data.model

data class Volunteer(
    val id: Int = 0,
    // Section 1
    val fullName: String,
    val dateOfBirth: String = "",
    val gender: String = "",
    val email: String,
    val mobileNumber: String,
    val physicalAddress: String = "",
    val cityTown: String = "",
    
    // Section 2
    val emergencyContactName: String = "",
    val relationshipToVolunteer: String = "",
    val emergencyContactNumber: String = "",
    
    // Section 3
    val interests: List<String> = emptyList(),
    
    // Section 4
    val occupation: String = "",
    val relevantSkills: String = "",
    val previousExperience: Boolean = false,
    val experienceDescription: String = "",
    val certifications: String = "",
    
    // Section 5
    val availability: List<String> = emptyList(),
    val hoursPerWeek: String = "",
    
    // Section 6
    val motivation: String = "",
    
    // Section 7
    val agreeToTerms: Boolean = false,
    val consentToStorage: Boolean = false,
    val consentToPhotos: Boolean = false,

    val signedUpAt: String = ""
)
