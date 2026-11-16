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

    suspend fun refreshProfile(): Member {
        val token = sessionStore.currentToken() ?: error("Please sign in again.")
        val response = callAuthApi { authApi.profile("Bearer $token") }
        check(sessionStore.currentToken() == token) { "Account changed. Please refresh." }
        return cacheRemoteMember(response.user ?: error("Profile missing."))
    }

    suspend fun updateSharedProfile(fullName: String, phoneNumber: String): Member {
        val token = sessionStore.currentToken() ?: error("Please sign in again.")
        val response = callAuthApi { authApi.updateProfile("Bearer $token", com.homeofhope.app.data.model.ProfileUpdateRequest(fullName, phoneNumber)) }
        check(sessionStore.currentToken() == token) { "Account changed. Please refresh." }
        return cacheRemoteMember(response.user ?: error("Profile missing."))
    }

    suspend fun googleAccount(idToken: String, uid: String, fullName: String = "", idNumber: String = "", password: String = ""): Member {
        val response = authApi.googleLogin(com.homeofhope.app.data.model.GoogleLoginRequest(idToken, fullName, idNumber, password))
        val body = response.body() ?: runCatching {
            Gson().fromJson(response.errorBody()?.string(), AuthResponse::class.java)
        }.getOrNull()
        if (!response.isSuccessful || body?.success != true) {
            throw GoogleAccountException(body?.action, body?.message ?: "Unable to sign in to your website account.")
        }
        val member = saveAuthenticatedMember(body)
        val linked = member.copy(firebaseUid = uid)
        memberDao.updateMember(linked)
        return linked
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
                joinDate = remote.joinDate,
                phoneNumber = remote.phoneNumber
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
                joinDate = remote.joinDate,
                phoneNumber = remote.phoneNumber
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
        sessionStore.clearIfToken(token)
        if (!token.isNullOrBlank()) {
            runCatching { authApi.logout("Bearer $token") }
        }
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

class GoogleAccountException(val action: String?, message: String) : Exception(message)
