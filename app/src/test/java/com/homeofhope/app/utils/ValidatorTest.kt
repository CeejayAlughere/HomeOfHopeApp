package com.homeofhope.app.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ValidatorTest {

    @Test
    fun `isValidName returns true for valid names`() {
        assertTrue(Validator.isValidName("John Smith"))
        assertTrue(Validator.isValidName("O'Connor"))
        assertTrue(Validator.isValidName("Jean-Luc"))
        assertTrue(Validator.isValidName("  John Smith  "))
    }

    @Test
    fun `isValidName returns false for invalid names`() {
        assertFalse(Validator.isValidName("J")) // Too short
        assertFalse(Validator.isValidName("John123")) // Contains numbers
        assertFalse(Validator.isValidName("John!")) // Contains symbols
        assertFalse(Validator.isValidName("")) // Empty
    }

    @Test
    fun `isValidPhone returns true for valid phones`() {
        assertTrue(Validator.isValidPhone("0821234567"))
        assertTrue(Validator.isValidPhone("+27821234567"))
        assertTrue(Validator.isValidPhone("123456789012345"))
    }

    @Test
    fun `isValidPhone returns false for invalid phones`() {
        assertFalse(Validator.isValidPhone("123456789")) // Too short (9 digits)
        assertFalse(Validator.isValidPhone("1234567890123456")) // Too long (16 digits)
        assertFalse(Validator.isValidPhone("082-123-4567")) // Contains hyphens
        assertFalse(Validator.isValidPhone("082abc4567")) // Contains letters
    }

    @Test
    fun `isValidAddress returns true for valid addresses`() {
        assertTrue(Validator.isValidAddress("123 Main St"))
        assertTrue(Validator.isValidAddress("Plot 45, Valley View"))
        assertTrue(Validator.isValidAddress("A/B/C Sector"))
    }

    @Test
    fun `isValidAddress returns false for invalid addresses`() {
        assertFalse(Validator.isValidAddress("123")) // Too short
        assertFalse(Validator.isValidAddress("Main!")) // Invalid char !
    }

    @Test
    fun `isValidCity returns true for valid cities`() {
        assertTrue(Validator.isValidCity("Cape Town"))
        assertTrue(Validator.isValidCity("Johannesburg"))
    }

    @Test
    fun `isValidCity returns false for invalid cities`() {
        assertFalse(Validator.isValidCity("Cape Town 1")) // Contains numbers
        assertFalse(Validator.isValidCity("NYC!")) // Contains symbols
        assertFalse(Validator.isValidCity("")) // Empty
    }

    @Test
    fun `isAtLeast16YearsOld returns correct values`() {
        val today = Calendar.getInstance()
        
        val sixteenYearsAgo = today.clone() as Calendar
        sixteenYearsAgo.add(Calendar.YEAR, -16)
        assertTrue(Validator.isAtLeast16YearsOld(sixteenYearsAgo.timeInMillis))

        val seventeenYearsAgo = today.clone() as Calendar
        seventeenYearsAgo.add(Calendar.YEAR, -17)
        assertTrue(Validator.isAtLeast16YearsOld(seventeenYearsAgo.timeInMillis))

        val fifteenYearsAgo = today.clone() as Calendar
        fifteenYearsAgo.add(Calendar.YEAR, -15)
        assertFalse(Validator.isAtLeast16YearsOld(fifteenYearsAgo.timeInMillis))
        
        val tomorrow = today.clone() as Calendar
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)
        assertFalse(Validator.isAtLeast16YearsOld(tomorrow.timeInMillis))
    }
}
