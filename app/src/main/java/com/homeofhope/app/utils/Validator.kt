package com.homeofhope.app.utils

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
}
