package com.homeofhope.app.utils

import java.util.Calendar

object Validator {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isNotBlank(value: String): Boolean {
        return value.isNotBlank()
    }
    
    fun isPositiveAmount(amount: String): Boolean {
        return amount.toDoubleOrNull()?.let { it > 0 } ?: false
    }

    fun isValidName(name: String): Boolean {
        // Only allow letters, spaces, apostrophes, and hyphens. Min 2 chars.
        val nameRegex = "^[A-Za-z\\s'-]{2,}$".toRegex()
        return nameRegex.matches(name.trim())
    }

    fun isValidPhone(phone: String): Boolean {
        // Numeric characters only, optional leading '+'. 10-15 digits.
        val phoneRegex = "^\\+?[0-9]{10,15}$".toRegex()
        return phoneRegex.matches(phone)
    }

    fun isValidAddress(address: String): Boolean {
        // Allow letters, numbers, commas, periods, slashes, and spaces. Min 5 chars.
        val addressRegex = "^[A-Za-z0-9\\s,./-]{5,}$".toRegex()
        return addressRegex.matches(address.trim())
    }

    fun isValidCity(city: String): Boolean {
        // Letters and spaces only.
        val cityRegex = "^[A-Za-z\\s]+$".toRegex()
        return cityRegex.matches(city.trim()) && city.trim().isNotEmpty()
    }

    fun isAtLeast16YearsOld(dobMillis: Long): Boolean {
        val dob = Calendar.getInstance().apply { timeInMillis = dobMillis }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age >= 16
    }
}
