package com.homeofhope.app.data.repository

import com.homeofhope.app.data.local.dao.MemberDao
import com.homeofhope.app.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {
    suspend fun registerMember(member: Member) {
        memberDao.insertMember(member)
    }

    suspend fun updateMember(member: Member) {
        memberDao.updateMember(member)
    }

    suspend fun getMemberByUsername(username: String): Member? {
        return memberDao.getMemberByUsername(username)
    }

    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()
}
