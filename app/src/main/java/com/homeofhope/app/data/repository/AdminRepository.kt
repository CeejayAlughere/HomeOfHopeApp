package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.*
import com.homeofhope.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class AdminRepository(
    private val adminDao: AdminDao,
    private val memberDao: MemberDao,
    private val volunteerHoursDao: VolunteerHoursDao,
    private val donationDao: DonationDao,
    private val eventDao: EventDao
) {
    // Admin Auth
    suspend fun getAdminByEmail(email: String) = adminDao.getAdminByEmail(email)
    suspend fun insertAdmin(admin: Admin) = adminDao.insertAdmin(admin)

    // Member Management
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()
    suspend fun updateMember(member: Member) = memberDao.updateMember(member)
    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)
    fun getMemberCount(): Flow<Int> = memberDao.getMemberCount()

    // Volunteer Hours
    fun getHoursByMember(memberId: Int): Flow<List<VolunteerHours>> = volunteerHoursDao.getHoursByMember(memberId)
    fun getTotalHoursByMember(memberId: Int): Flow<Double?> = volunteerHoursDao.getTotalHoursByMember(memberId)
    suspend fun insertVolunteerHours(hours: VolunteerHours) = volunteerHoursDao.insertHours(hours)
    suspend fun updateVolunteerHours(hours: VolunteerHours) = volunteerHoursDao.updateHours(hours)
    suspend fun deleteVolunteerHours(hours: VolunteerHours) = volunteerHoursDao.deleteHours(hours)
    fun getTotalHoursAll(): Flow<Double?> = volunteerHoursDao.getTotalHoursAll()

    // Stats
    fun getTotalDonationsAll(): Flow<Double?> = donationDao.getTotalDonationsAll()
    
    val allEvents: Flow<List<Event>> = eventDao.getAllUpcomingEvents()
    
    fun getAllDonations(): Flow<List<DonationRecord>> = donationDao.getAllDonations()
}
