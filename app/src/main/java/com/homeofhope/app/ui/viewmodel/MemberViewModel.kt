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
import com.homeofhope.app.data.repository.AnnouncementRepository
import com.homeofhope.app.data.repository.EventRepository
import com.homeofhope.app.data.repository.ImpactStoryRepository
import com.homeofhope.app.data.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val announcementRepository: AnnouncementRepository,
    private val impactStoryRepository: ImpactStoryRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUserFlow = MutableStateFlow<Member?>(null)
    var currentUser by mutableStateOf<Member?>(null)
        private set

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
                    password = password
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
                currentUser = member
                _currentUserFlow.value = member
                _authState.value = AuthState.Success(member)
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
