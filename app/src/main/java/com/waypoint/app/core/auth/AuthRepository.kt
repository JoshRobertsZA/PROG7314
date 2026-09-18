package com.waypoint.app.core.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
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
     * GitHub has no native Android sign-in SDK (unlike Google's Credential
     * Manager), so this goes through Firebase's generic OAuthProvider flow:
     * a Chrome Custom Tab opens GitHub's own consent page, then redirects
     * back into the app via a scheme Firebase registers automatically at
     * build time (no manifest changes needed here). Requires GitHub to be
     * enabled as a sign-in provider in the Firebase console first - see
     * README for the console-side setup steps.
     */
    suspend fun signInWithGitHub(activity: Activity): FirebaseUser {
        val provider = OAuthProvider.newBuilder("github.com").build()
        val result = auth.startActivityForSignInWithProvider(activity, provider).await()
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    }

    /**
     * If the app process was killed mid-redirect (e.g. the OS reclaimed
     * memory while GitHub's consent page was in front), the in-flight
     * OAuthProvider result is still recoverable here once the app resumes -
     * call this from the same place restoreSessionIfSignedIn() is called.
     * Returns null when there's nothing pending, which is the common case.
     */
    suspend fun recoverPendingGitHubSignIn(): FirebaseUser? =
        auth.pendingAuthResult?.await()?.user

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
