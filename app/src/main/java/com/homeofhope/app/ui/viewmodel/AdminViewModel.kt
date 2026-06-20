package com.homeofhope.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.data.local.entity.VolunteerHours
import com.homeofhope.app.data.repository.AdminRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminDashboardStats(
    val totalMembers: Int = 0,
    val totalVolunteers: Int = 0, // Members with at least 1 hour
    val totalDonations: Double = 0.0,
    val totalVolunteerHours: Double = 0.0,
    val activeEvents: Int = 0,
    val newRegistrationsThisMonth: Int = 0
)

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _members = repository.allMembers
    val members: StateFlow<List<Member>> = _members.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dashboardStats: StateFlow<AdminDashboardStats> = combine(
        repository.getMemberCount(),
        repository.getTotalDonationsAll(),
        repository.getTotalHoursAll(),
        repository.allEvents,
        repository.allMembers
    ) { memberCount, totalDonations, totalHours, events, allMembers ->
        AdminDashboardStats(
            totalMembers = memberCount,
            totalVolunteers = allMembers.count { it.status == "Active" }, // Placeholder logic
            totalDonations = totalDonations ?: 0.0,
            totalVolunteerHours = totalHours ?: 0.0,
            activeEvents = events.size,
            newRegistrationsThisMonth = allMembers.count { 
                val calendar = java.util.Calendar.getInstance()
                val currentMonth = calendar.get(java.util.Calendar.MONTH)
                val currentYear = calendar.get(java.util.Calendar.YEAR)
                calendar.timeInMillis = it.joinDate
                calendar.get(java.util.Calendar.MONTH) == currentMonth && calendar.get(java.util.Calendar.YEAR) == currentYear
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminDashboardStats())

    fun updateMember(member: Member) {
        viewModelScope.launch {
            repository.updateMember(member)
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    fun addVolunteerHours(memberId: Int, eventName: String, hours: Double, date: Long, notes: String) {
        viewModelScope.launch {
            repository.insertVolunteerHours(
                VolunteerHours(
                    memberId = memberId,
                    eventName = eventName,
                    hours = hours,
                    serviceDate = date,
                    notes = notes
                )
            )
        }
    }

    fun getHoursForMember(memberId: Int): Flow<List<VolunteerHours>> = repository.getHoursByMember(memberId)

    fun updateVolunteerHours(hours: VolunteerHours) {
        viewModelScope.launch {
            repository.updateVolunteerHours(hours)
        }
    }

    fun deleteVolunteerHours(hours: VolunteerHours) {
        viewModelScope.launch {
            repository.deleteVolunteerHours(hours)
        }
    }
}
