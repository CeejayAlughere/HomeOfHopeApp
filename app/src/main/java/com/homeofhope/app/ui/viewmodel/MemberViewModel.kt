package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.local.entity.Announcement
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.ImpactStory
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.data.local.entity.CommunityMessage
import com.homeofhope.app.data.local.entity.DonationRecord
import com.homeofhope.app.data.model.Badge
import com.homeofhope.app.data.model.MembershipLevel
import com.homeofhope.app.data.repository.*
import com.homeofhope.app.utils.MembershipCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.content.Context
import android.util.Patterns
import com.homeofhope.app.auth.GoogleIdentity
import com.homeofhope.app.auth.GoogleSignInHelper
import com.google.firebase.auth.FirebaseAuth

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val member: Member) : AuthState()
    data
    class Error(val message: String) : AuthState()
    object Registered : AuthState()
}

sealed class GoogleAuthState {
    object Idle : GoogleAuthState()
    object Loading : GoogleAuthState()

    data class ExistingMember(val member: Member) : GoogleAuthState()

    data class ProfileRequired(
        val email: String,
        val displayName: String
    ) : GoogleAuthState()

    data class Error(val message: String) : GoogleAuthState()
}

class MemberViewModel(
    private val repository: MemberRepository,
    private val eventRepository: EventRepository,
    private val donationRepository: DonationRepository,
    private val communityRepository: CommunityRepository,
    private val announcementRepository: AnnouncementRepository,
    private val impactStoryRepository: ImpactStoryRepository,
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _googleAuthState =
        MutableStateFlow<GoogleAuthState>(GoogleAuthState.Idle)

    val googleAuthState: StateFlow<GoogleAuthState> =
        _googleAuthState.asStateFlow()

    private var pendingGoogleIdentity: GoogleIdentity? = null

    private val _currentUserFlow = MutableStateFlow<Member?>(null)
    var currentUser by mutableStateOf<Member?>(null)
        private set

    // Only the website login supplies a token accepted by the donation API.
    var isWebsiteSession by mutableStateOf(false)
        private set

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentMemberStatus: StateFlow<String> = _currentUserFlow.flatMapLatest { user ->
        user?.let { repository.getMemberStatus(it.id).map { status -> status ?: "Active" } } ?: flowOf("Active")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Active")

    val upcomingEvents: StateFlow<List<Event>> = eventRepository.getAllUpcomingEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<Announcement>> = announcementRepository.getAllAnnouncements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val impactStories: StateFlow<List<ImpactStory>> = impactStoryRepository.getAllImpactStories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val joinedEvents: StateFlow<List<Event>> = _currentUserFlow.flatMapLatest { user ->
        user?.let { eventRepository.getJoinedEventsForMember(it.id) } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val totalDonations: StateFlow<Double> = _currentUserFlow.flatMapLatest { user ->
        user?.let { donationRepository.getDonationsForMember(it.id) }?.map { it.sumOf { d -> d.amount } } ?: flowOf(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val totalHours: StateFlow<Double> = _currentUserFlow.flatMapLatest { user ->
        user?.let { 
            combine(
                eventRepository.getTotalHoursForMember(it.id).map { h -> h ?: 0.0 },
                adminRepository.getTotalHoursByMember(it.id).map { h -> h ?: 0.0 }
            ) { eventHours, adminHours ->
                eventHours + adminHours
            }
        } ?: flowOf(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val membershipLevel: StateFlow<MembershipLevel> = combine(totalDonations, totalHours, joinedEvents) { donations, hours, events ->
        MembershipCalculator.calculateLevel(donations, hours, events.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MembershipLevel.SEED_SUPPORTER)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val badges: StateFlow<List<Badge>> = combine(_currentUserFlow, totalDonations, totalHours, joinedEvents, membershipLevel) { user, donations, hours, events, level ->
        val firstDonationDate = user?.let { u ->
            // We can't easily get the first donation date here without another flow or a suspend call
            // But for the purpose of the badge, if totalDonations > 0, it means they have donated at least once.
            if (donations > 0) System.currentTimeMillis() else null
        }
        MembershipCalculator.getBadges(donations, hours, events.size, firstDonationDate, level)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityMessages: StateFlow<List<CommunityMessage>> = communityRepository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendMessage(content: String) {
        val user = currentUser ?: return
        viewModelScope.launch {
            communityRepository.sendMessage(
                CommunityMessage(
                    memberId = user.id,
                    memberName = user.fullName,
                    content = content
                )
            )
        }
    }

    fun completeEvent(eventId: Int, hours: Double) {
        val user = currentUser ?: return
        viewModelScope.launch {
            eventRepository.updateHours(user.id, eventId, hours)
        }
    }

    fun isJoined(eventId: Int): Flow<Boolean> {
        val userId = currentUser?.id ?: return flowOf(false)
        return eventRepository.isMemberJoined(userId, eventId)
    }

    fun toggleJoinEvent(eventId: Int, isCurrentlyJoined: Boolean) {
        val user = currentUser ?: return
        viewModelScope.launch {
            if (isCurrentlyJoined) {
                eventRepository.leaveEvent(user.id, eventId)
            } else {
                eventRepository.joinEvent(user.id, eventId)
            }
        }
    }

    fun updatePaymentInfo(bankName: String, cardNumber: String, expiryDate: String) {
        val user = currentUser ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(
                savedBankName = bankName,
                savedCardNumber = cardNumber,
                savedExpiryDate = expiryDate
            )
            repository.updateMember(updatedUser)
            currentUser = updatedUser
            _currentUserFlow.value = updatedUser
        }
    }

    var profileMessage by mutableStateOf<String?>(null)
        private set
    var profileSaving by mutableStateOf(false)
        private set

    fun refreshSharedProfile() {
        val memberId = currentUser?.id ?: return
        viewModelScope.launch {
            try {
                val member = repository.refreshProfile()
                if (currentUser?.id == memberId) {
                    currentUser = member
                    _currentUserFlow.value = member
                }
            } catch (e: kotlinx.coroutines.CancellationException) { throw e } catch (e: Exception) { profileMessage = e.message ?: "Could not refresh profile." }
        }
    }

    fun updateProfile(fullName: String, email: String, phoneNumber: String) {
        val user = currentUser ?: return
        if (email != user.email) { profileMessage = "Email changes require account verification."; return }
        if (profileSaving) return
        profileSaving = true
        profileMessage = null
        viewModelScope.launch {
            try {
                val updated = repository.updateSharedProfile(fullName.trim(), phoneNumber.trim())
                if (currentUser?.id == user.id) {
                    currentUser = updated
                    _currentUserFlow.value = updated
                    profileMessage = "Profile saved to your shared account."
                }
            } catch (e: kotlinx.coroutines.CancellationException) { throw e } catch (e: Exception) { profileMessage = e.message ?: "Could not save profile." } finally { profileSaving = false }
        }
    }

    fun register(fullName: String, idNumber: String, email: String, password: String, confirmPass: String) {
        isWebsiteSession = false
        if (fullName.isBlank() || idNumber.isBlank() || email.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }

        if (idNumber.length != 13 || !idNumber.all { it.isDigit() }) {
            _authState.value = AuthState.Error("ID number must be 13 digits")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }

        if (password.length < 8) {
            _authState.value = AuthState.Error("Password must contain at least 8 characters")
            return
        }

        if (password != confirmPass) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.registerAccount(
                    fullName = fullName,
                    idNumber = idNumber,
                    email = email,
                    password = password
                )
                _authState.value = AuthState.Registered
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Registration failed. Please try again."
                )
            }
        }
    }

    fun login(identifier: String, password: String) {
        isWebsiteSession = false
        if (identifier.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error(
                "Please enter your username/email and password"
            )
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val member = repository.loginAccount(identifier, password)
                if (member.status == "Suspended") {
                    _authState.value = AuthState.Error("Your account has been suspended. Please contact support.")
                } else {
                    currentUser = member
                    _currentUserFlow.value = member
                    isWebsiteSession = true
                    _authState.value = AuthState.Success(member)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Login failed. Please try again."
                )
            }
        }
    }



    var googleNeedsPassword by mutableStateOf(false)
        private set

    fun signInWithGoogle(context: Context) {
        isWebsiteSession = false
        pendingGoogleIdentity = null
        googleNeedsPassword = false
        viewModelScope.launch {
            _googleAuthState.value = GoogleAuthState.Loading
            try {
                val identity = GoogleSignInHelper.signIn(context)
                pendingGoogleIdentity = identity
                finishGoogleSignIn(identity, "", "", "")
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _googleAuthState.value = GoogleAuthState.Error(e.message ?: "Google sign-in failed.")
            }
        }
    }

    fun completeGoogleProfile(fullName: String, idNumber: String, password: String = "") {
        val identity = pendingGoogleIdentity ?: run {
            _googleAuthState.value = GoogleAuthState.Error("Return to login and sign in with Google again.")
            return
        }
        viewModelScope.launch {
            _googleAuthState.value = GoogleAuthState.Loading
            try {
                finishGoogleSignIn(identity, fullName.trim(), idNumber.trim(), password)
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _googleAuthState.value = GoogleAuthState.Error(e.message ?: "Unable to complete Google sign-in.")
            }
        }
    }

    private suspend fun finishGoogleSignIn(identity: GoogleIdentity, fullName: String, idNumber: String, password: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
            ?: error("Sign in with Google again.")
        check(firebaseUser.uid == identity.uid) { "Google account changed. Sign in again." }
        val idToken = firebaseUser.getIdToken(true).await().token ?: error("Google token unavailable.")
        val member = try {
            repository.googleAccount(idToken, identity.uid, fullName, idNumber, password)
        } catch (e: GoogleAccountException) {
            if (e.action == "link" || e.action == "profile") {
                googleNeedsPassword = e.action == "link"
                if (fullName.isEmpty() && idNumber.isEmpty() && password.isEmpty()) {
                    _googleAuthState.value = GoogleAuthState.ProfileRequired(identity.email, identity.displayName)
                } else {
                    _googleAuthState.value = GoogleAuthState.Error(e.message ?: "Please check your details.")
                }
                return
            }
            throw e
        }
        currentUser = member
        _currentUserFlow.value = member
        isWebsiteSession = true
        pendingGoogleIdentity = null
        _googleAuthState.value = GoogleAuthState.ExistingMember(member)
    }

    fun clearGoogleAuthState() {
        _googleAuthState.value = GoogleAuthState.Idle
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
    
    fun logout() {
        isWebsiteSession = false
        currentUser = null
        _currentUserFlow.value = null
        donationRepository.clearHistory()
        _authState.value = AuthState.Idle
        FirebaseAuth.getInstance().signOut()
        viewModelScope.launch {
            repository.clearSession()
        }
    }
}
