package com.waypoint.app.core.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.waypoint.app.core.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

/**
 * Thin wrapper around Firebase Authentication: Google, GitHub and email sign-in, re-authentication for
 * account deletion, ID tokens for the API, and sign-out.
 */
object AuthRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isSignedIn: Boolean
        get() = currentUser != null

    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    }

    suspend fun signInWithGitHub(activity: Activity): FirebaseUser {
        val provider = OAuthProvider.newBuilder("github.com").build()
        val result = auth.startActivityForSignInWithProvider(activity, provider).await()
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    }

    // Firebase has no separate "sign up" credential type - creating the
    // account and signing in are the same call. The display name is set as
    // a second step because createUserWithEmailAndPassword doesn't accept one.
    suspend fun signUpWithEmail(email: String, password: String, displayName: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("Firebase sign-up succeeded but returned no user")
        if (displayName.isNotBlank()) {
            val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
            user.updateProfile(profileUpdate).await()
        }
        return user
    }

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    }

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun updateDisplayName(name: String) {
        val user = auth.currentUser ?: error("No signed-in user to update")
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user.updateProfile(profileUpdate).await()
    }

    // The providerId list on a user who deleted all but one provider still
    // reports that provider plus "firebase" (the identity itself), so this
    // is only ever "password" / "google.com" / "github.com" from the real
    // sign-in methods, never empty for a signed-in user.
    val currentSignInProviderIds: List<String>
        get() = auth.currentUser?.providerData?.map { it.providerId } ?: emptyList()

    suspend fun reauthenticateWithEmail(password: String) {
        val user = auth.currentUser ?: error("No signed-in user to reauthenticate")
        val email = user.email ?: error("No email on the current user")
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    suspend fun reauthenticateWithGoogleIdToken(idToken: String) {
        val user = auth.currentUser ?: error("No signed-in user to reauthenticate")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        user.reauthenticate(credential).await()
    }

    suspend fun deleteCurrentUser() {
        val user = auth.currentUser ?: error("No signed-in user to delete")
        user.delete().await()
    }

    suspend fun recoverPendingGitHubSignIn(): FirebaseUser? =
        auth.pendingAuthResult?.await()?.user

    suspend fun getIdToken(forceRefresh: Boolean = false): String? =
        auth.currentUser?.getIdToken(forceRefresh)?.await()?.token

    private val signOutScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Signs out of Firebase and clears Credential Manager's state, so the next
     * Google sign-in shows the account picker instead of silently reusing the old account.
     */
    fun signOut(context: Context) {
        auth.signOut()
        AppSettings.onSignedOut()
        val appContext = context.applicationContext
        signOutScope.launch {
            runCatching {
                CredentialManager.create(appContext).clearCredentialState(ClearCredentialStateRequest())
            }.onFailure { Log.w("AuthRepository", "clearCredentialState failed: ${it.message}") }
        }
    }
}
