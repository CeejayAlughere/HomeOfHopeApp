package com.homeofhope.app.ui.screens.volunteer

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.homeofhope.app.data.model.Volunteer
import com.homeofhope.app.ui.components.PageHeader
import com.homeofhope.app.ui.theme.HopeGreen
import com.homeofhope.app.ui.theme.HopeWhite
import com.homeofhope.app.ui.viewmodel.VolunteerViewModel
import com.homeofhope.app.ui.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

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
        1 -> fullName.isNotBlank() && email.isNotBlank() && mobile.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        2 -> emergencyName.isNotBlank() && emergencyNumber.isNotBlank()
        3 -> selectedInterests.isNotEmpty()
        4 -> occupation.isNotBlank() && (!hasExperience || experienceDesc.isNotBlank())
        5 -> selectedAvailability.isNotEmpty() && hoursPerWeek.isNotBlank()
        6 -> motivation.length >= 20
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
        val datePickerState = rememberDatePickerState()
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
                                    fullName, { fullName = it },
                                    dob, { showDatePicker = true },
                                    gender, { gender = it },
                                    email, { email = it },
                                    mobile, { mobile = it },
                                    address, { address = it },
                                    city, { city = it }
                                )
                                2 -> EmergencyContactSection(
                                    emergencyName, { emergencyName = it },
                                    relationship, { relationship = it },
                                    emergencyNumber, { emergencyNumber = it }
                                )
                                3 -> InterestsSection(interests, selectedInterests)
                                4 -> SkillsExperienceSection(
                                    occupation, { occupation = it },
                                    skills, { skills = it },
                                    hasExperience, { hasExperience = it },
                                    experienceDesc, { experienceDesc = it },
                                    certifications, { certifications = it }
                                )
                                5 -> AvailabilitySection(availabilityOptions, selectedAvailability, hoursPerWeek, { hoursPerWeek = it })
                                6 -> MotivationSection(motivation, { motivation = it })
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
                                                    fullName = fullName,
                                                    dateOfBirth = if (dob != 0L) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dob)) else "",
                                                    gender = gender,
                                                    email = email,
                                                    mobileNumber = mobile,
                                                    physicalAddress = address,
                                                    cityTown = city,
                                                    emergencyContactName = emergencyName,
                                                    relationshipToVolunteer = relationship,
                                                    emergencyContactNumber = emergencyNumber,
                                                    interests = selectedInterests.toList(),
                                                    occupation = occupation,
                                                    relevantSkills = skills,
                                                    previousExperience = hasExperience,
                                                    experienceDescription = experienceDesc,
                                                    certifications = certifications,
                                                    availability = selectedAvailability.toList(),
                                                    hoursPerWeek = hoursPerWeek,
                                                    motivation = motivation,
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
        isError = name.isBlank() && name.isNotEmpty()
    )
    
    val dateText = if (dob != 0L) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dob)) else ""
    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        label = { Text("Date of Birth") },
        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = HopeGreen) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        readOnly = true,
        shape = RoundedCornerShape(16.dp),
        enabled = true,
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
        isError = email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    )
    
    VolunteerTextField(
        value = mobile, 
        onValueChange = onMobileChange, 
        label = "Mobile Number*", 
        icon = Icons.Default.Phone, 
        keyboardType = KeyboardType.Phone
    )
    
    VolunteerTextField(value = address, onValueChange = onAddressChange, label = "Physical Address", icon = Icons.Default.Home)
    VolunteerTextField(value = city, onValueChange = onCityChange, label = "City/Town", icon = Icons.Default.LocationCity)
}

@Composable
fun EmergencyContactSection(
    name: String, onNameChange: (String) -> Unit,
    relationship: String, onRelChange: (String) -> Unit,
    number: String, onNumChange: (String) -> Unit
) {
    SectionTitle("Emergency Contact", "In case of any incidents during volunteering events.", Icons.Default.ContactPhone)
    
    VolunteerTextField(value = name, onValueChange = onNameChange, label = "Contact Name*", icon = Icons.Default.Person)
    VolunteerTextField(value = relationship, onValueChange = onRelChange, label = "Relationship", icon = Icons.Default.People)
    VolunteerTextField(value = number, onValueChange = onNumChange, label = "Contact Number*", icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
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
    VolunteerTextField(value = skills, onValueChange = onSkillsChange, label = "Relevant Skills", icon = Icons.Default.Architecture, singleLine = false, minLines = 3)
    
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Switch(checked = hasExp, onCheckedChange = onExpChange, colors = SwitchDefaults.colors(checkedThumbColor = HopeGreen))
        Spacer(modifier = Modifier.width(12.dp))
        Text("Previous Volunteer Experience?", style = MaterialTheme.typography.bodyMedium)
    }
    
    if (hasExp) {
        VolunteerTextField(value = expDesc, onValueChange = onDescChange, label = "Describe your experience*", icon = Icons.Default.History, singleLine = false, minLines = 3)
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
    VolunteerTextField(value = hours, onValueChange = onHoursChange, label = "Expected Hours Per Week*", icon = Icons.Default.Timer, keyboardType = KeyboardType.Number)
}

@Composable
fun MotivationSection(motivation: String, onMotChange: (String) -> Unit) {
    SectionTitle("Motivation", "Your 'why' is important to us.", Icons.Default.EmojiObjects)
    VolunteerTextField(
        value = motivation, 
        onValueChange = onMotChange, 
        label = "Why do you want to volunteer? (Min 20 chars)*", 
        icon = Icons.AutoMirrored.Filled.Comment, 
        singleLine = false, 
        minLines = 5,
        supportingText = "Character count: ${motivation.length}"
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
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = if (isError) MaterialTheme.colorScheme.error else HopeGreen) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HopeGreen,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = HopeGreen
        )
    )
}
