package com.waypoint.app.core.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.secrets.RemoteSecrets
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Gets a Google ID token through Credential Manager (replacing the deprecated GoogleSignIn library)
 * for Firebase to sign in with.
 */
object GoogleSignInHelper {

    suspend fun requestIdToken(context: Context, filterByAuthorizedAccounts: Boolean): String {
        val credentialManager = CredentialManager.create(context)
        val webClientId = RemoteSecrets.ensureWebClientIdLoaded().ifBlank { BuildConfig.FIREBASE_WEB_CLIENT_ID }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        check(
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) { "Unexpected credential type: ${credential.type}" }

        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }
}
