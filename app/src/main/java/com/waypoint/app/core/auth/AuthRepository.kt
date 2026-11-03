// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `android.app.Activity` for use in this file
import android.app.Activity
// imports `com.google.firebase.auth.FirebaseAuth` for use in this file
import com.google.firebase.auth.FirebaseAuth
// imports `com.google.firebase.auth.FirebaseUser` for use in this file
import com.google.firebase.auth.FirebaseUser
// imports `com.google.firebase.auth.GoogleAuthProvider` for use in this file
import com.google.firebase.auth.GoogleAuthProvider
// imports `com.google.firebase.auth.OAuthProvider` for use in this file
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
// imports `kotlinx.coroutines.tasks.await` for use in this file
import kotlinx.coroutines.tasks.await

// declares object `AuthRepository` and opens its body
object AuthRepository {

    // declares private read-only property `auth` of type `FirebaseAuth`, delegated to `lazy { FirebaseAuth.getInstance() }`
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // declares read-only property `currentUser` of type `FirebaseUser?`
    val currentUser: FirebaseUser?
        // continues the statement started above: `get() = auth.currentUser`
        get() = auth.currentUser

    // declares read-only property `isSignedIn` of type `Boolean`
    val isSignedIn: Boolean
        // custom getter: returns `currentUser != null`
        get() = currentUser != null

    // declares suspend function `signInWithGoogleIdToken` taking 1 parameter (`idToken`), returning `FirebaseUser` and opens its body
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        // declares read-only property `credential`, initialised with the result of calling `GoogleAuthProvider.getCredential(…)`
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        // declares read-only property `result`, initialised with the result of calling `auth.signInWithCredential(…)`
        val result = auth.signInWithCredential(credential).await()
        // returns `result.user ?: error("Firebase sign-in succeeded but returned no user…` from the current function
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    // closes the function `signInWithGoogleIdToken`
    }

    // declares suspend function `signInWithGitHub` taking 1 parameter (`activity`), returning `FirebaseUser` and opens its body
    suspend fun signInWithGitHub(activity: Activity): FirebaseUser {
        // declares read-only property `provider`, initialised with the result of calling `OAuthProvider.newBuilder(…)`
        val provider = OAuthProvider.newBuilder("github.com").build()
        // declares read-only property `result`, initialised with the result of calling `auth.startActivityForSignInWithProvider(…)`
        val result = auth.startActivityForSignInWithProvider(activity, provider).await()
        // returns `result.user ?: error("Firebase sign-in succeeded but returned no user…` from the current function
        return result.user ?: error("Firebase sign-in succeeded but returned no user")
    // closes the function `signInWithGitHub`
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

    // declares suspend function `recoverPendingGitHubSignIn` taking no parameters, returning `FirebaseUser?`; its body is the expression ``
    suspend fun recoverPendingGitHubSignIn(): FirebaseUser? =
        // continues the statement started above: `auth.pendingAuthResult?.await()?.user`
        auth.pendingAuthResult?.await()?.user

    // declares suspend function `getIdToken` taking 1 parameter (`forceRefresh`), returning `String?`; its body is the expression ``
    suspend fun getIdToken(forceRefresh: Boolean = false): String? =
        // continues the statement started above: `auth.currentUser?.getIdToken(forceRefresh)?.await()?.token`
        auth.currentUser?.getIdToken(forceRefresh)?.await()?.token

    // declares function `signOut` taking no parameters and opens its body
    fun signOut() {
        // calls `signOut` on `auth` with arguments `()`
        auth.signOut()
    // closes the function `signOut`
    }
// closes the object `AuthRepository`
}
