
package com.homeofhope.app.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

data class GoogleIdentity(
    val uid: String,
    val email: String,
    val displayName: String
)

object GoogleSignInHelper {

    suspend fun signIn(context: Context): GoogleIdentity {
        val webClientId = context.getString(
            com.homeofhope.app.R.string.default_web_client_id
        )

        val googleOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        val result = try {
            credentialManager.getCredential(
                context = context,
                request = request
            )
        } catch (e: NoCredentialException) {
            throw NoCredentialException(
                "No Google account is available for sign-in. " +
                        "Add a Google account in your device settings, or sign in with email."
            ).apply { initCause(e) }
        }

        val credential = result.credential

        if (
            credential !is CustomCredential ||
            credential.type !=
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            error("Google did not return a valid ID token credential.")
        }

        val googleCredential =
            GoogleIdTokenCredential.createFrom(credential.data)

        val firebaseCredential = GoogleAuthProvider.getCredential(
            googleCredential.idToken,
            null
        )

        val authResult = FirebaseAuth.getInstance()
            .signInWithCredential(firebaseCredential)
            .await()

        val firebaseUser = authResult.user
            ?: error("Firebase could not identify the signed-in user.")

        val email = firebaseUser.email
            ?: error("The Google account did not provide an email address.")

        return GoogleIdentity(
            uid = firebaseUser.uid,
            email = email,
            displayName = firebaseUser.displayName.orEmpty()
        )
    }
}
