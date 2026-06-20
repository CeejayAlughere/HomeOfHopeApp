package com.homeofhope.app.data.local.dao

import androidx.room.*
import com.homeofhope.app.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: Member)

    @androidx.room.Update
    suspend fun updateMember(member: Member)

    @Query("SELECT * FROM members WHERE username = :username LIMIT 1")
    suspend fun getMemberByUsername(username: String): Member?

    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members")
    suspend fun getAllMembersOnce(): List<Member>

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT COUNT(*) FROM members")
    fun getMemberCount(): Flow<Int>
}
