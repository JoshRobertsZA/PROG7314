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
 * Launches the modern Credential Manager "Sign in with Google" sheet and
 * returns the Google ID token, which [AuthRepository] then exchanges for a
 * Firebase session.
 *
 * Needs the OAuth "Web client ID" Firebase issues once Google sign-in is
 * enabled for the project. A non-blank FIREBASE_WEB_CLIENT_ID in
 * apikey.properties always wins (see apikey.properties.example); otherwise
 * this falls back to the shared team key cache via [RemoteSecrets], same
 * as every other API key in this project - so a fresh clone works with
 * zero local setup. Sourced from BuildConfig/RemoteSecrets rather than a
 * google-services.json-generated resource so the app still compiles for
 * teammates who haven't dropped that file in yet.
 */
object GoogleSignInHelper {

    /**
     * [filterByAuthorizedAccounts] set to true only shows accounts that
     * have already signed into this app before - good for a quiet
     * "resume session" prompt, but it throws NoCredentialException the
     * first time a user signs in, so callers should retry with false on
     * that failure (see AuthViewModel).
     */
    suspend fun requestIdToken(context: Context, filterByAuthorizedAccounts: Boolean): String {
        val credentialManager = CredentialManager.create(context)
        val webClientId = RemoteSecrets.get("FIREBASE_WEB_CLIENT_ID", BuildConfig.FIREBASE_WEB_CLIENT_ID)

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
