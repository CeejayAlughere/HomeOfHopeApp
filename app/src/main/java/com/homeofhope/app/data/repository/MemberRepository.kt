package com.homeofhope.app.data.repository

import com.google.gson.Gson
import com.homeofhope.app.data.local.AuthSessionStore
import com.homeofhope.app.data.local.dao.MemberDao
import com.homeofhope.app.data.local.entity.Member
import com.homeofhope.app.data.model.AuthResponse
import com.homeofhope.app.data.model.AuthUserDto
import com.homeofhope.app.data.model.LoginRequest
import com.homeofhope.app.data.model.RegisterRequest
import com.homeofhope.app.data.network.AuthApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.IOException

class MemberRepository(
    private val memberDao: MemberDao,
    private val authApi: AuthApiService,
    private val sessionStore: AuthSessionStore
) {

    suspend fun registerAccount(
        fullName: String,
        idNumber: String,
        email: String,
        password: String
    ): Member {
        val response = callAuthApi {
            authApi.register(
                RegisterRequest(
                    fullName = fullName.trim(),
                    idNumber = idNumber.trim(),
                    email = email.trim().lowercase(),
                    username = email.trim().lowercase(),
                    password = password
                )
            )
        }

        return saveAuthenticatedMember(response)
    }

    suspend fun loginAccount(identifier: String, password: String): Member {
        val response = callAuthApi {
            authApi.login(
                LoginRequest(
                    identifier = identifier.trim(),
                    password = password
                )
            )
        }

        return saveAuthenticatedMember(response)
    }

    private suspend fun callAuthApi(
        request: suspend () -> Response<AuthResponse>
    ): AuthResponse {
        val response = try {
            request()
        } catch (e: IOException) {
            throw AccountServiceException(
                "Cannot reach the Home of Hope server. " +
                    "Make sure Apache and MySQL are running in XAMPP."
            )
        }

        val body = response.body()

        if (response.isSuccessful && body?.success == true) {
            return body
        }

        val apiMessage = body?.message
            ?.takeIf { it.isNotBlank() }
            ?: parseErrorMessage(response)

        throw AccountServiceException(
            apiMessage ?: "The account request was unsuccessful."
        )
    }

    private fun parseErrorMessage(
        response: Response<AuthResponse>
    ): String? {
        val rawError = response.errorBody()?.string()
            ?.takeIf { it.isNotBlank() }
            ?: return null

        return runCatching {
            Gson().fromJson(rawError, AuthResponse::class.java).message
        }.getOrNull()?.takeIf { it.isNotBlank() }
    }

    private suspend fun saveAuthenticatedMember(
        response: AuthResponse
    ): Member {
        val remoteUser = response.user
            ?: throw AccountServiceException(
                "The server did not return a member profile."
            )

        val token = response.token
            ?: throw AccountServiceException(
                "The server did not return a login token."
            )

        val member = cacheRemoteMember(remoteUser)
        sessionStore.save(token, remoteUser.id)
        return member
    }

    private suspend fun cacheRemoteMember(remote: AuthUserDto): Member {
        val existing = memberDao.getMemberByEmail(remote.email)
            ?: memberDao.getMemberByUsername(remote.username)

        val cached = if (existing == null) {
            Member(
                fullName = remote.fullName,
                email = remote.email,
                idNumber = remote.idNumber,
                username = remote.username,
                password = "",
                memberNumber = remote.memberNumber,
                status = remote.status,
                joinDate = remote.joinDate
            )
        } else {
            existing.copy(
                fullName = remote.fullName,
                email = remote.email,
                idNumber = remote.idNumber,
                username = remote.username,
                password = "",
                memberNumber = remote.memberNumber,
                status = remote.status,
                joinDate = remote.joinDate
            )
        }

        if (existing == null) {
            memberDao.insertMember(cached)
        } else {
            memberDao.updateMember(cached)
        }

        return memberDao.getMemberByEmail(remote.email)
            ?: memberDao.getMemberByUsername(remote.username)
            ?: throw AccountServiceException(
                "Your account was verified but could not be cached on this device."
            )
    }

    suspend fun clearSession() {
        val token = sessionStore.currentToken()

        if (!token.isNullOrBlank()) {
            runCatching {
                authApi.logout("Bearer $token")
            }
        }

        sessionStore.clear()
    }

    // Existing local profile operations remain available for the app.
    suspend fun registerMember(member: Member) {
        memberDao.insertMember(member)
    }

    suspend fun updateMember(member: Member) {
        memberDao.updateMember(member)
    }

    suspend fun getMemberByUsername(username: String): Member? {
        return memberDao.getMemberByUsername(username)
    }

    suspend fun getMemberById(id: Int): Member? {
        return memberDao.getAllMembersOnce().find { it.id == id }
    }

    fun getMemberStatus(id: Int): Flow<String?> =
        memberDao.getMemberStatus(id)

    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun getMemberByFirebaseUid(firebaseUid: String): Member? {
        return memberDao.getMemberByFirebaseUid(firebaseUid)
    }

    suspend fun getMemberByEmail(email: String): Member? {
        return memberDao.getMemberByEmail(email)
    }

    suspend fun getMemberByIdNumber(idNumber: String): Member? {
        return memberDao.getMemberByIdNumber(idNumber)
    }
}

class AccountServiceException(message: String) : Exception(message)
