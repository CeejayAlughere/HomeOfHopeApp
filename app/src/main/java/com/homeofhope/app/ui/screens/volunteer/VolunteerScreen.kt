package com.homeofhope.app.ui.screens.volunteer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.homeofhope.app.data.model.Volunteer
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.UiState
import com.homeofhope.app.ui.viewmodel.VolunteerViewModel
import com.homeofhope.app.utils.Validator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerScreen(viewModel: VolunteerViewModel) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 7
    
    // Form State
    var fullName by remember { mutableStateOf("") }
    var dob by remember { mutableLongStateOf(0L) }
    var showDatePicker by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    
    var emergencyName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var emergencyNumber by remember { mutableStateOf("") }
    
    val interests = listOf("Child Care", "Mentorship", "Teaching & Tutoring", "Food Distribution", "Fundraising", "Event Assistance", "Administrative Support", "Community Outreach", "Marketing & Social Media")
    val selectedInterests = remember { mutableStateListOf<String>() }
    
    var occupation by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var hasExperience by remember { mutableStateOf(false) }
    var experienceDesc by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }
    
    val availabilityOptions = listOf("Weekdays", "Weekends", "Evenings", "Flexible")
    val selectedAvailability = remember { mutableStateListOf<String>() }
    var hoursPerWeek by remember { mutableStateOf("") }
    
    var motivation by remember { mutableStateOf("") }
    
    var agreeToTerms by remember { mutableStateOf(false) }
    var consentStorage by remember { mutableStateOf(false) }
    var consentPhotos by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()

    // Validation Logic
    val isStepValid = when (currentStep) {
        1 -> Validator.isValidName(fullName) && 
             Validator.isValidEmail(email) && 
             Validator.isValidPhone(mobile) &&
             dob != 0L && Validator.isAtLeast16YearsOld(dob) &&
             Validator.isValidAddress(address) &&
             Validator.isValidCity(city)
        2 -> Validator.isValidName(emergencyName) && 
             Validator.isValidPhone(emergencyNumber) &&
             relationship.isNotBlank()
        3 -> selectedInterests.isNotEmpty()
        4 -> occupation.isNotBlank() && (!hasExperience || experienceDesc.trim().length >= 20)
        5 -> selectedAvailability.isNotEmpty() && 
             hoursPerWeek.toIntOrNull()?.let { it in 1..80 } == true
        6 -> motivation.trim().length in 30..1000
        7 -> agreeToTerms && consentStorage
        else -> true
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            snackbarHostState.showSnackbar("Welcome to the family! We'll contact you soon.")
            viewModel.resetState()
            currentStep = 1
            // Reset fields
            fullName = ""; dob = 0L; gender = ""; email = ""; mobile = ""; address = ""; city = ""
            emergencyName = ""; relationship = ""; emergencyNumber = ""
            selectedInterests.clear(); occupation = ""; skills = ""; hasExperience = false; experienceDesc = ""; certifications = ""
            selectedAvailability.clear(); hoursPerWeek = ""; motivation = ""
            agreeToTerms = false; consentStorage = false; consentPhotos = false
        } else if (uiState is UiState.Error) {
            snackbarHostState.showSnackbar("Error: ${uiState.message}")
            viewModel.resetState()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dob = datePickerState.selectedDateMillis ?: 0L
                    showDatePicker = false
                },) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            
            Column(modifier = Modifier.padding(16.dp)) {
                VolunteerProgressBar(currentStep, totalSteps)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                        }.using(SizeTransform(clip = false))
                    }, label = "stepAnimation"
                ) { step ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        val isLastStep = step == totalSteps
                        Column(modifier = Modifier.padding(24.dp)) {
                            when (step) {
                                1 -> PersonalInfoSection(
                                    fullName, { 
                                        if (it.all { c -> c.isLetter() || c.isWhitespace() || c == '\'' || c == '-' }) {
                                            fullName = it 
                                        }
                                    },
                                    dob, { showDatePicker = true },
                                    gender, { gender = it },
                                    email, { email = it.trim() },
                                    mobile, { 
                                        if (it.all { c -> c.isDigit() || (it.indexOf(c) == 0 && c == '+') }) {
                                            mobile = it 
                                        }
                                    },
                                    address, { address = it },
                                    city, { 
                                        if (it.all { c -> c.isLetter() || c.isWhitespace() }) {
                                            city = it 
                                        }
                                    }
                                )
                                2 -> EmergencyContactSection(
                                    emergencyName, { 
                                        if (it.all { c -> c.isLetter() || c.isWhitespace() || c == '\'' || c == '-' }) {
                                            emergencyName = it 
                                        }
                                    },
                                    relationship, { relationship = it },
                                    emergencyNumber, { 
                                        if (it.all { c -> c.isDigit() || (it.indexOf(c) == 0 && c == '+') }) {
                                            emergencyNumber = it 
                                        }
                                    }
                                )
                                3 -> InterestsSection(interests, selectedInterests)
                                4 -> SkillsExperienceSection(
                                    occupation, { occupation = it },
                                    skills, { if (it.length <= 500) skills = it },
                                    hasExperience, { hasExperience = it },
                                    experienceDesc, { experienceDesc = it },
                                    certifications, { certifications = it }
                                )
                                5 -> AvailabilitySection(availabilityOptions, selectedAvailability, hoursPerWeek, { 
                                    if (it.all { c -> c.isDigit() }) {
                                        hoursPerWeek = it 
                                    }
                                })
                                6 -> MotivationSection(motivation, { if (it.length <= 1000) motivation = it })
                                7 -> ConsentSection(
                                    agreeToTerms, { agreeToTerms = it },
                                    consentStorage, { consentStorage = it },
                                    consentPhotos, { consentPhotos = it }
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (currentStep > 1) {
                                    OutlinedButton(
                                        onClick = { currentStep-- },
                                        modifier = Modifier.weight(1f).height(56.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text("Back")
                                    }
                                }
                                
                                Button(
                                    onClick = {
                                        if (isLastStep) {
                                            viewModel.submitVolunteer(
                                                Volunteer(
                                                    fullName = fullName.trim(),
                                                    dateOfBirth = if (dob != 0L) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dob)) else "",
                                                    gender = gender,
                                                    email = email.trim(),
                                                    mobileNumber = mobile.trim(),
                                                    physicalAddress = address.trim(),
                                                    cityTown = city.trim(),
                                                    emergencyContactName = emergencyName.trim(),
                                                    relationshipToVolunteer = relationship,
                                                    emergencyContactNumber = emergencyNumber.trim(),
                                                    interests = selectedInterests.toList(),
                                                    occupation = occupation.trim(),
                                                    relevantSkills = skills.trim(),
                                                    previousExperience = hasExperience,
                                                    experienceDescription = experienceDesc.trim(),
                                                    certifications = certifications.trim(),
                                                    availability = selectedAvailability.toList(),
                                                    hoursPerWeek = hoursPerWeek,
                                                    motivation = motivation.trim(),
                                                    agreeToTerms = agreeToTerms,
                                                    consentToStorage = consentStorage,
                                                    consentToPhotos = consentPhotos
                                                )
                                            )
                                        } else {
                                            currentStep++
                                        }
                                    },
                                    modifier = Modifier.weight(if (currentStep > 1) 1f else 2f).height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = HopeGreen),
                                    shape = RoundedCornerShape(16.dp),
                                    enabled = (uiState !is UiState.Loading) && isStepValid
                                ) {
                                    if (uiState is UiState.Loading) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = HopeWhite)
                                    } else {
                                        Text(if (isLastStep) "Become a Volunteer" else "Next Step", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun VolunteerProgressBar(currentStep: Int, totalSteps: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Step $currentStep of $totalSteps",
                style = MaterialTheme.typography.labelLarge,
                color = HopeGreen,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(currentStep * 100) / totalSteps}% Complete",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { currentStep.toFloat() / totalSteps.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = HopeGreen,
            trackColor = HopeGreen.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun SectionTitle(title: String, description: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = HopeGreen, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun PersonalInfoSection(
    name: String, onNameChange: (String) -> Unit,
    dob: Long, onDobClick: () -> Unit,
    gender: String, onGenderChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    mobile: String, onMobileChange: (String) -> Unit,
    address: String, onAddressChange: (String) -> Unit,
    city: String, onCityChange: (String) -> Unit
) {
    SectionTitle("Personal Information", "Tell us who you are so we can get in touch.", Icons.Default.Person)
    
    VolunteerTextField(
        value = name, 
        onValueChange = onNameChange, 
        label = "Full Name*", 
        icon = Icons.Default.Badge,
        isError = name.isNotEmpty() && !Validator.isValidName(name),
        errorText = if (name.isNotEmpty() && !Validator.isValidName(name)) "Only letters, spaces, apostrophes, and hyphens allowed (min 2 chars)." else null
    )
    
    val dateText = if (dob != 0L) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dob)) else ""
    val isAgeValid = dob == 0L || Validator.isAtLeast16YearsOld(dob)
    
    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        label = { Text("Date of Birth*") },
        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = if (isAgeValid) HopeGreen else MaterialTheme.colorScheme.error) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        readOnly = true,
        shape = RoundedCornerShape(16.dp),
        isError = !isAgeValid,
        supportingText = if (!isAgeValid) { { Text("You must be at least 16 years old.") } } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HopeGreen,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLeadingIconColor = HopeGreen
        ),
        trailingIcon = {
            IconButton(onClick = onDobClick) {
                Icon(Icons.Default.EditCalendar, contentDescription = "Select Date", tint = HopeGreen)
            }
        }
    )
    
    Text("Gender (Optional)", style = MaterialTheme.typography.labelMedium, color = HopeGreen, modifier = Modifier.padding(top = 8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
        listOf("Male", "Female", "Other").forEach { option ->
            FilterChip(
                selected = gender == option,
                onClick = { onGenderChange(if (gender == option) "" else option) },
                label = { Text(option) }
            )
        }
    }
    
    VolunteerTextField(
        value = email, 
        onValueChange = onEmailChange, 
        label = "Email Address*", 
        icon = Icons.Default.Email, 
        keyboardType = KeyboardType.Email,
        isError = email.isNotEmpty() && !Validator.isValidEmail(email),
        errorText = if (email.isNotEmpty() && !Validator.isValidEmail(email)) "Please enter a valid email address." else null
    )
    
    VolunteerTextField(
        value = mobile, 
        onValueChange = onMobileChange, 
        label = "Mobile Number*", 
        icon = Icons.Default.Phone, 
        keyboardType = KeyboardType.Phone,
        isError = mobile.isNotEmpty() && !Validator.isValidPhone(mobile),
        errorText = if (mobile.isNotEmpty() && !Validator.isValidPhone(mobile)) "Please enter a valid 10-15 digit phone number." else null
    )
    
    VolunteerTextField(
        value = address, 
        onValueChange = onAddressChange, 
        label = "Physical Address*", 
        icon = Icons.Default.Home,
        isError = address.isNotEmpty() && !Validator.isValidAddress(address),
        errorText = if (address.isNotEmpty() && !Validator.isValidAddress(address)) "Min 5 characters. Letters, numbers, and basic punctuation allowed." else null
    )
    
    VolunteerTextField(
        value = city, 
        onValueChange = onCityChange, 
        label = "City/Town*", 
        icon = Icons.Default.LocationCity,
        isError = city.isNotEmpty() && !Validator.isValidCity(city),
        errorText = if (city.isNotEmpty() && !Validator.isValidCity(city)) "Letters and spaces only." else null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactSection(
    name: String, onNameChange: (String) -> Unit,
    relationship: String, onRelChange: (String) -> Unit,
    number: String, onNumChange: (String) -> Unit
) {
    SectionTitle("Emergency Contact", "In case of any incidents during volunteering events.", Icons.Default.ContactPhone)
    
    VolunteerTextField(
        value = name, 
        onValueChange = onNameChange, 
        label = "Contact Name*", 
        icon = Icons.Default.Person,
        isError = name.isNotEmpty() && !Validator.isValidName(name),
        errorText = if (name.isNotEmpty() && !Validator.isValidName(name)) "Only letters, spaces, apostrophes, and hyphens allowed." else null
    )
    
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Parent", "Guardian", "Sibling", "Spouse", "Friend", "Other")
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = relationship,
            onValueChange = {},
            readOnly = true,
            label = { Text("Relationship*") },
            leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = HopeGreen) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HopeGreen,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onRelChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
    
    VolunteerTextField(
        value = number, 
        onValueChange = onNumChange, 
        label = "Contact Number*", 
        icon = Icons.Default.Phone, 
        keyboardType = KeyboardType.Phone,
        isError = number.isNotEmpty() && !Validator.isValidPhone(number),
        errorText = if (number.isNotEmpty() && !Validator.isValidPhone(number)) "Please enter a valid 10-15 digit phone number." else null
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsSection(options: List<String>, selected: MutableList<String>) {
    SectionTitle("Volunteer Interests", "Choose the areas where you feel you can contribute most.", Icons.Default.Favorite)
    
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected.contains(option),
                onClick = {
                    if (selected.contains(option)) selected.remove(option) else selected.add(option)
                },
                label = { Text(option) },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HopeGreen.copy(alpha = 0.2f),
                    selectedLabelColor = HopeGreen
                )
            )
        }
    }
}

@Composable
fun SkillsExperienceSection(
    occ: String, onOccChange: (String) -> Unit,
    skills: String, onSkillsChange: (String) -> Unit,
    hasExp: Boolean, onExpChange: (Boolean) -> Unit,
    expDesc: String, onDescChange: (String) -> Unit,
    cert: String, onCertChange: (String) -> Unit
) {
    SectionTitle("Skills & Experience", "Your background helps us place you in the right role.", Icons.Default.Build)
    
    VolunteerTextField(value = occ, onValueChange = onOccChange, label = "Occupation*", icon = Icons.Default.Work)
    VolunteerTextField(
        value = skills, 
        onValueChange = onSkillsChange, 
        label = "Relevant Skills (Optional)", 
        icon = Icons.Default.Architecture, 
        singleLine = false, 
        minLines = 3,
        supportingText = "${skills.length}/500"
    )
    
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Switch(checked = hasExp, onCheckedChange = onExpChange, colors = SwitchDefaults.colors(checkedThumbColor = HopeGreen))
        Spacer(modifier = Modifier.width(12.dp))
        Text("Previous Volunteer Experience?", style = MaterialTheme.typography.bodyMedium)
    }
    
    if (hasExp) {
        VolunteerTextField(
            value = expDesc, 
            onValueChange = onDescChange, 
            label = "Describe your experience*", 
            icon = Icons.Default.History, 
            singleLine = false, 
            minLines = 3,
            isError = expDesc.isNotEmpty() && expDesc.trim().length < 20,
            errorText = if (expDesc.isNotEmpty() && expDesc.trim().length < 20) "Minimum 20 characters required." else null
        )
    }
    
    VolunteerTextField(value = cert, onValueChange = onCertChange, label = "Certifications (Optional)", icon = Icons.Default.Verified)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailabilitySection(options: List<String>, selected: MutableList<String>, hours: String, onHoursChange: (String) -> Unit) {
    SectionTitle("Availability", "Knowing your schedule helps us plan our community activities.", Icons.Default.Schedule)
    
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected.contains(option),
                onClick = {
                    if (selected.contains(option)) selected.remove(option) else selected.add(option)
                },
                label = { Text(option) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    val isHoursValid = hours.isEmpty() || (hours.toIntOrNull()?.let { it in 1..80 } == true)
    VolunteerTextField(
        value = hours, 
        onValueChange = onHoursChange, 
        label = "Expected Hours Per Week*", 
        icon = Icons.Default.Timer, 
        keyboardType = KeyboardType.Number,
        isError = !isHoursValid,
        errorText = if (!isHoursValid) "Please enter a value between 1 and 80." else null
    )
}

@Composable
fun MotivationSection(motivation: String, onMotChange: (String) -> Unit) {
    SectionTitle("Motivation", "Your 'why' is important to us.", Icons.Default.EmojiObjects)
    VolunteerTextField(
        value = motivation, 
        onValueChange = onMotChange, 
        label = "Why do you want to volunteer?*", 
        icon = Icons.AutoMirrored.Filled.Comment, 
        singleLine = false, 
        minLines = 5,
        isError = motivation.isNotEmpty() && (motivation.trim().length < 30 || motivation.length > 1000),
        errorText = if (motivation.isNotEmpty() && motivation.trim().length < 30) "Minimum 30 characters required." else null,
        supportingText = "${motivation.length}/1000"
    )
}

@Composable
fun ConsentSection(
    terms: Boolean, onTermsChange: (Boolean) -> Unit,
    storage: Boolean, onStorageChange: (Boolean) -> Unit,
    photos: Boolean, onPhotosChange: (Boolean) -> Unit
) {
    SectionTitle("Consent & Agreement", "Final step to join our community.", Icons.AutoMirrored.Filled.FactCheck)
    
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            ConsentItem(checked = terms, onCheckedChange = onTermsChange, text = "I agree to the Terms and Conditions.*")
            ConsentItem(checked = storage, onCheckedChange = onStorageChange, text = "I consent to my information being stored.*")
            ConsentItem(checked = photos, onCheckedChange = onPhotosChange, text = "I consent to promotional photography/video.")
        }
    }
}

@Composable
fun ConsentItem(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun VolunteerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isError: Boolean = false,
    errorText: String? = null,
    supportingText: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = if (isError) MaterialTheme.colorScheme.error else HopeGreen) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HopeGreen,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLabelColor = HopeGreen
            )
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp).align(Alignment.End)
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
