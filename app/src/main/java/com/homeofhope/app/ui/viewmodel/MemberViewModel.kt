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
import android.content.Context
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

    fun updateProfile(fullName: String, email: String, phoneNumber: String) {
        val user = currentUser ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber
            )
            repository.updateMember(updatedUser)
            currentUser = updatedUser
            _currentUserFlow.value = updatedUser
        }
    }

    fun register(fullName: String, idNumber: String, username: String, password: String, confirmPass: String) {
        if (fullName.isBlank() || idNumber.isBlank() || username.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }

        if (idNumber.length != 13 || !idNumber.all { it.isDigit() }) {
            _authState.value = AuthState.Error("ID number must be 13 digits")
            return
        }

        if (password != confirmPass) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existing = repository.getMemberByUsername(username)
            if (existing != null) {
                _authState.value = AuthState.Error("Username already exists")
            } else {
                val newMember = Member(
                    fullName = fullName,
                    idNumber = idNumber,
                    username = username,
                    password = password,
                    memberNumber = MembershipCalculator.generateMemberId(),
                    joinDate = System.currentTimeMillis()
                )
                repository.registerMember(newMember)
                _authState.value = AuthState.Registered
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please enter username and password")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val member = repository.getMemberByUsername(username)
            if (member != null && member.password == password) {
                if (member.status == "Suspended") {
                    _authState.value = AuthState.Error("Your account has been suspended. Please contact support.")
                } else {
                    currentUser = member
                    _currentUserFlow.value = member
                    _authState.value = AuthState.Success(member)
                }
            } else {
                _authState.value = AuthState.Error("Invalid username or password")
            }
        }
    }



    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _googleAuthState.value = GoogleAuthState.Loading

            try {
                val identity = GoogleSignInHelper.signIn(context)

                // Match returning Google members by Firebase UID.
                val existing = repository.getMemberByFirebaseUid(identity.uid)

                if (existing != null) {
                    if (existing.status == "Suspended") {
                        FirebaseAuth.getInstance().signOut()
                        _googleAuthState.value = GoogleAuthState.Error(
                            "Your account has been suspended. Please contact support."
                        )
                    } else {
                        currentUser = existing
                        _currentUserFlow.value = existing
                        _googleAuthState.value =
                            GoogleAuthState.ExistingMember(existing)
                    }
                    return@launch
                }

                // Never silently link a Google account to an old account
                // using email alone.
                val emailMember = repository.getMemberByEmail(identity.email)

                if (emailMember != null) {
                    FirebaseAuth.getInstance().signOut()
                    _googleAuthState.value = GoogleAuthState.Error(
                        "An account already uses this email. Sign in with your existing account or contact support to link it."
                    )
                    return@launch
                }

                pendingGoogleIdentity = identity

                _googleAuthState.value = GoogleAuthState.ProfileRequired(
                    email = identity.email,
                    displayName = identity.displayName
                )
            } catch (e: Exception) {
                _googleAuthState.value = GoogleAuthState.Error(
                    e.localizedMessage ?: "Google sign-in failed. Please try again."
                )
            }
        }
    }

    fun completeGoogleProfile(
        fullName: String,
        idNumber: String
    ) {
        viewModelScope.launch {
            val identity = pendingGoogleIdentity

            if (identity == null) {
                _googleAuthState.value = GoogleAuthState.Error(
                    "Your Google sign-in session has expired. Please try again."
                )
                return@launch
            }

            val cleanedName = fullName.trim()
            val cleanedId = idNumber.trim()

            if (cleanedName.isBlank()) {
                _googleAuthState.value =
                    GoogleAuthState.Error("Please enter your full name.")
                return@launch
            }

            if (cleanedId.length != 13 || !cleanedId.all { it.isDigit() }) {
                _googleAuthState.value =
                    GoogleAuthState.Error("ID number must be 13 digits.")
                return@launch
            }

            try {
                _googleAuthState.value = GoogleAuthState.Loading

                val existingIdMember = repository.getMemberByIdNumber(cleanedId)

                if (existingIdMember != null) {
                    _googleAuthState.value = GoogleAuthState.Error(
                        "This ID number is already registered. Please sign in to your existing account or contact support."
                    )
                    return@launch
                }

                val member = Member(
                    fullName = cleanedName,
                    email = identity.email,
                    idNumber = cleanedId,
                    username = identity.email,
                    password = "",
                    memberNumber = MembershipCalculator.generateMemberId(),
                    joinDate = System.currentTimeMillis(),
                    firebaseUid = identity.uid
                )

                repository.registerMember(member)

                val savedMember =
                    repository.getMemberByFirebaseUid(identity.uid)
                        ?: throw IllegalStateException(
                            "Your profile was saved, but could not be loaded."
                        )

                currentUser = savedMember
                _currentUserFlow.value = savedMember
                pendingGoogleIdentity = null

                _googleAuthState.value =
                    GoogleAuthState.ExistingMember(savedMember)
            } catch (e: Exception) {
                _googleAuthState.value = GoogleAuthState.Error(
                    e.localizedMessage ?: "Could not save your member profile."
                )
            }
        }
    }

    fun clearGoogleAuthState() {
        _googleAuthState.value = GoogleAuthState.Idle
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
    
    fun logout() {
        currentUser = null
        _currentUserFlow.value = null
        _authState.value = AuthState.Idle
    }
}
