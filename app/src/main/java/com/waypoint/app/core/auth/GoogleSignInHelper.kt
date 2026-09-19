// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `androidx.credentials.CredentialManager` for use in this file
import androidx.credentials.CredentialManager
// imports `androidx.credentials.CustomCredential` for use in this file
import androidx.credentials.CustomCredential
// imports `androidx.credentials.GetCredentialRequest` for use in this file
import androidx.credentials.GetCredentialRequest
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.google.android.libraries.identity.googleid.GetGoogleIdOption` for use in this file
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
// imports `com.google.android.libraries.identity.googleid.GoogleIdTokenCredential` for use in this file
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

// declares object `GoogleSignInHelper` and opens its body
object GoogleSignInHelper {

    // declares suspend function `requestIdToken` taking 2 parameters (`context`, `filterByAuthorizedAccounts`), returning `String` and opens its body
    suspend fun requestIdToken(context: Context, filterByAuthorizedAccounts: Boolean): String {
        // declares read-only property `credentialManager`, initialised with the result of calling `CredentialManager.create(…)`
        val credentialManager = CredentialManager.create(context)
        // declares read-only property `webClientId`, initialised with the result of calling `RemoteSecrets.get(…)`
        val webClientId = RemoteSecrets.get("FIREBASE_WEB_CLIENT_ID", BuildConfig.FIREBASE_WEB_CLIENT_ID)

        // declares read-only property `googleIdOption`, initialised with the result of calling `GetGoogleIdOption.Builder(…)`
        val googleIdOption = GetGoogleIdOption.Builder()
            // chained call `.setFilterByAuthorizedAccounts` on the previous result with arguments `(filterByAuthorizedAccounts)`
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            // chained call `.setServerClientId` on the previous result with arguments `(webClientId)`
            .setServerClientId(webClientId)
            // chained call `.setAutoSelectEnabled` on the previous result with arguments `(false)`
            .setAutoSelectEnabled(false)
            // chained call `.build` on the previous result
            .build()

        // declares read-only property `request`, initialised with the result of calling `GetCredentialRequest.Builder(…)`
        val request = GetCredentialRequest.Builder()
            // chained call `.addCredentialOption` on the previous result with arguments `(googleIdOption)`
            .addCredentialOption(googleIdOption)
            // chained call `.build` on the previous result
            .build()

        // declares read-only property `result`, initialised with the result of calling `credentialManager.getCredential(…)`
        val result = credentialManager.getCredential(context, request)
        // declares read-only property `credential`, initialised to `result.credential`
        val credential = result.credential

        // calls `check` with an argument list that continues on the following lines
        check(
            // continues the statement started above: `credential is CustomCredential &&`
            credential is CustomCredential &&
                // continues the statement started above: `credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_T…`
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        // continues the statement started above: `) { "Unexpected credential type: ${credential.type}" }`
        ) { "Unexpected credential type: ${credential.type}" }

        // returns `GoogleIdTokenCredential.createFrom(credential.data).idToken` from the current function
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    // closes the function `requestIdToken`
    }
// closes the object `GoogleSignInHelper`
}
