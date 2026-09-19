// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `android.app.Activity` for use in this file
import android.app.Activity
// imports `android.app.Application` for use in this file
import android.app.Application
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `androidx.credentials.exceptions.GetCredentialCancellationException` for use in this file
import androidx.credentials.exceptions.GetCredentialCancellationException
// imports `androidx.credentials.exceptions.NoCredentialException` for use in this file
import androidx.credentials.exceptions.NoCredentialException
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.db.AccountEntity` for use in this file
import com.waypoint.app.core.db.AccountEntity
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.core.notifications.WelcomeNotifier` for use in this file
import com.waypoint.app.core.notifications.WelcomeNotifier
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `com.google.firebase.auth.FirebaseAuthWebException` for use in this file
import com.google.firebase.auth.FirebaseAuthWebException
// imports `com.google.firebase.auth.FirebaseUser` for use in this file
import com.google.firebase.auth.FirebaseUser
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.asStateFlow` for use in this file
import kotlinx.coroutines.flow.asStateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// declares enum class `AuthProvider` and opens its body
enum class AuthProvider {
    // expression: `GOOGLE, GITHUB`
    GOOGLE, GITHUB
// closes the class `AuthProvider`
}

// expression: `data class AuthUiState(`
data class AuthUiState(
    // continues the statement started above: `val isLoading: Boolean = false,`
    val isLoading: Boolean = false,
    // continues the statement started above: `val loadingProvider: AuthProvider? = null,`
    val loadingProvider: AuthProvider? = null,
    // continues the statement started above: `val errorMessage: String? = null,`
    val errorMessage: String? = null,
// closes the multi-line argument list started above
)

// declares class `AuthViewModel` with a primary constructor taking 1 parameter (`application`), inheriting from `AndroidViewModel(application)` and opens its body
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // declares private read-only property `tripRepository`, initialised with the result of calling `TripRepository(…)`
    private val tripRepository = TripRepository(application)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(AuthUiState())
    // declares read-only property `uiState` of type `StateFlow<AuthUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // declares suspend function `restoreSessionIfSignedIn` taking no parameters and opens its body
    suspend fun restoreSessionIfSignedIn() {
        // expression: `AuthRepository.currentUser?.let { applySignedInUser(it); return }`
        AuthRepository.currentUser?.let { applySignedInUser(it); return }
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // calls `recoverPendingGitHubSignIn` on `AuthRepository` with arguments `()`, then chains `.?.let { applySignedInUser(it)…`
            AuthRepository.recoverPendingGitHubSignIn()?.let { applySignedInUser(it) }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "No recoverable GitHub sign-in", e)`
            Log.w(TAG, "No recoverable GitHub sign-in", e)
        // closes the catch block
        }
    // closes the function `restoreSessionIfSignedIn`
    }

    // declares function `signInWithGoogle` taking 2 parameters (`context`, `onSuccess`) and opens its body
    fun signInWithGoogle(context: Context, onSuccess: () -> Unit) {
        // `if` statement: executes `return` when `_uiState.value.isLoading` is true
        if (_uiState.value.isLoading) return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true, loadingProvider = Au…`
            _uiState.update { it.copy(isLoading = true, loadingProvider = AuthProvider.GOOGLE, errorMessage = null) }
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // calls `ensureLoaded` on `RemoteSecrets` with arguments `()`
                RemoteSecrets.ensureLoaded()
                // declares read-only property `idToken`, initialised with the result of calling `fetchGoogleIdToken(…)`
                val idToken = fetchGoogleIdToken(context)
                // declares read-only property `user`, initialised with the result of calling `AuthRepository.signInWithGoogleIdToken(…)`
                val user = AuthRepository.signInWithGoogleIdToken(idToken)
                // calls `applySignedInUser` with arguments `(user)`
                applySignedInUser(user)
                // expression: `_uiState.update { it.copy(isLoading = false, loadingProvider = n…`
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
                // calls `onSuccess` with arguments `()`
                onSuccess()
            // `catch` block: handles a thrown `GetCredentialCancellationException` bound to `e`
            } catch (e: GetCredentialCancellationException) {
                // expression: `_uiState.update { it.copy(isLoading = false, loadingProvider = n…`
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "Google sign-in failed", e)`
                Log.w(TAG, "Google sign-in failed", e)
                // opens a block after `_uiState.update`
                _uiState.update {
                    // calls `copy` on `it` with arguments `(isLoading = false, loadingProvider = null, e…)`
                    it.copy(isLoading = false, loadingProvider = null, errorMessage = "Couldn't sign in with Google. Please try again.")
                // closes the block
                }
            // closes the catch block
            }
        // closes the block
        }
    // closes the function `signInWithGoogle`
    }

    // declares function `signInWithGitHub` taking 2 parameters (`activity`, `onSuccess`) and opens its body
    fun signInWithGitHub(activity: Activity, onSuccess: () -> Unit) {
        // `if` statement: executes `return` when `_uiState.value.isLoading` is true
        if (_uiState.value.isLoading) return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true, loadingProvider = Au…`
            _uiState.update { it.copy(isLoading = true, loadingProvider = AuthProvider.GITHUB, errorMessage = null) }
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // declares read-only property `user`, initialised with the result of calling `AuthRepository.signInWithGitHub(…)`
                val user = AuthRepository.signInWithGitHub(activity)
                // calls `applySignedInUser` with arguments `(user)`
                applySignedInUser(user)
                // expression: `_uiState.update { it.copy(isLoading = false, loadingProvider = n…`
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
                // calls `onSuccess` with arguments `()`
                onSuccess()
            // `catch` block: handles a thrown `FirebaseAuthWebException` bound to `e`
            } catch (e: FirebaseAuthWebException) {
                // expression: `_uiState.update { it.copy(isLoading = false, loadingProvider = n…`
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "GitHub sign-in failed", e)`
                Log.w(TAG, "GitHub sign-in failed", e)
                // opens a block after `_uiState.update`
                _uiState.update {
                    // calls `copy` on `it` with arguments `(isLoading = false, loadingProvider = null, e…)`
                    it.copy(isLoading = false, loadingProvider = null, errorMessage = "Couldn't sign in with GitHub. Please try again.")
                // closes the block
                }
            // closes the catch block
            }
        // closes the block
        }
    // closes the function `signInWithGitHub`
    }

    // declares private suspend function `fetchGoogleIdToken` taking 1 parameter (`context`), returning `String`; its body is the expression ``
    private suspend fun fetchGoogleIdToken(context: Context): String =
        // continues the statement started above: `try {`
        try {
            // calls `requestIdToken` on `GoogleSignInHelper` with arguments `(context, filterByAuthorizedAccounts = true)`
            GoogleSignInHelper.requestIdToken(context, filterByAuthorizedAccounts = true)
        // `catch` block: handles a thrown `NoCredentialException` bound to `e`
        } catch (e: NoCredentialException) {
            // calls `requestIdToken` on `GoogleSignInHelper` with arguments `(context, filterByAuthorizedAccounts = false)`
            GoogleSignInHelper.requestIdToken(context, filterByAuthorizedAccounts = false)
        // closes the catch block
        }

    // declares private function `applySignedInUser` taking 1 parameter (`user`) and opens its body
    private fun applySignedInUser(user: FirebaseUser) {
        // calls `signIn` on `SessionManager` with an argument list that continues on the following lines
        SessionManager.signIn(
            // continues the statement started above: `id = user.uid,`
            id = user.uid,
            // continues the statement started above: `email = user.email.orEmpty(),`
            email = user.email.orEmpty(),
            // continues the statement started above: `displayName = user.displayName.orEmpty(),`
            displayName = user.displayName.orEmpty(),
            // continues the statement started above: `photoUrl = user.photoUrl?.toString().orEmpty(),`
            photoUrl = user.photoUrl?.toString().orEmpty(),
        // closes the multi-line argument list started above
        )
        // calls `notifyIfNeeded` on `WelcomeNotifier` with arguments `(getApplication())`
        WelcomeNotifier.notifyIfNeeded(getApplication())
        // calls `launch` on `viewModelScope` with arguments `(Dispatchers.IO)` and opens a trailing lambda / block
        viewModelScope.launch(Dispatchers.IO) {
            // calls `upsertAccount` on `tripRepository` with an argument list that continues on the following lines
            tripRepository.upsertAccount(
                // continues the statement started above: `AccountEntity(`
                AccountEntity(
                    // continues the statement started above: `id = user.uid,`
                    id = user.uid,
                    // continues the statement started above: `email = user.email.orEmpty(),`
                    email = user.email.orEmpty(),
                    // continues the statement started above: `displayName = user.displayName.orEmpty(),`
                    displayName = user.displayName.orEmpty(),
                    // continues the statement started above: `photoUrl = user.photoUrl?.toString().orEmpty(),`
                    photoUrl = user.photoUrl?.toString().orEmpty(),
                    // continues the statement started above: `lastLoginMs = System.currentTimeMillis(),`
                    lastLoginMs = System.currentTimeMillis(),
                // closes the multi-line argument list started above
                )
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `launch`
        }
    // closes the function `applySignedInUser`
    }

    // declares function `signOut` taking no parameters and opens its body
    fun signOut() {
        // calls `signOut` on `AuthRepository` with arguments `()`
        AuthRepository.signOut()
        // calls `signOut` on `SessionManager` with arguments `()`
        SessionManager.signOut()
        // calls `reset` on `WelcomeNotifier` with arguments `()`
        WelcomeNotifier.reset()
    // closes the function `signOut`
    }

    // declares the companion object holding members shared by all instances of the enclosing class
    private companion object {
        // declares const read-only property `TAG`, initialised to the string literal "AuthViewModel"
        const val TAG = "AuthViewModel"
    // closes the companion object
    }
// closes the class `AuthViewModel`
}
