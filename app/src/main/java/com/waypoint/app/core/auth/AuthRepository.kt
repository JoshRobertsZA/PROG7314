package com.waypoint.app.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * Thin wrapper around Firebase Auth. Every screen that needs to sign a
 * user in, out, or fetch the token to authorize a call to the team's REST
 * API should go through here rather than touching [FirebaseAuth] directly.
 */
object AuthRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /** Null when nobody is signed in - check this on app start to skip onboarding. */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isSignedIn: Boolean
        get() = currentUser != null

    /** Exchanges a Google ID token (from Credential Manager) for a signed-in Firebase user. */
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    }

    /**
     * The Firebase ID token for the current user, to send as a Bearer token
     * on requests to the team's REST API so it can verify the caller with
     * the Firebase Admin SDK. Null if nobody is signed in.
     */
    suspend fun getIdToken(forceRefresh: Boolean = false): String? =
        auth.currentUser?.getIdToken(forceRefresh)?.await()?.token

    fun signOut() {
        auth.signOut()
    }
}
