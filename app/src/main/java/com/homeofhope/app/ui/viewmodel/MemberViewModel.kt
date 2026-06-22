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

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val member: Member) : AuthState()
    data class Error(val message: String) : AuthState()
    object Registered : AuthState()
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

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
    
    fun logout() {
        currentUser = null
        _currentUserFlow.value = null
        _authState.value = AuthState.Idle
    }
}
