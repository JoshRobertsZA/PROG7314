package com.waypoint.app.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary

enum class EmailAuthMode { SIGN_IN, SIGN_UP, RESET }

sealed interface EmailAuthSubmission {
    data class SignUp(val displayName: String, val email: String, val password: String, val confirmPassword: String) : EmailAuthSubmission
    data class SignIn(val email: String, val password: String) : EmailAuthSubmission
    data class Reset(val email: String) : EmailAuthSubmission
}

@Composable
fun EmailAuthScreen(
    onBackClick: () -> Unit,
    onSubmit: (EmailAuthSubmission) -> Unit,
    modifier: Modifier = Modifier,
    initialMode: EmailAuthMode = EmailAuthMode.SIGN_IN,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    resetEmailSent: Boolean = false,
) {
    var mode by remember { mutableStateOf(initialMode) }
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp)) {
            Text(
                text = stringResource(titleFor(mode)),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.edit_itinerary_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            if (mode == EmailAuthMode.SIGN_UP) {
                LabeledField(
                    label = stringResource(R.string.auth_display_name_label),
                    value = displayName,
                    onValueChange = { displayName = it },
                    hint = stringResource(R.string.auth_display_name_hint),
                )
            }

            LabeledField(
                label = stringResource(R.string.auth_email_label),
                value = email,
                onValueChange = { email = it },
                hint = stringResource(R.string.auth_email_hint),
                keyboardType = KeyboardType.Email,
            )

            if (mode == EmailAuthMode.RESET) {
                Text(
                    text = stringResource(R.string.auth_reset_instructions),
                    color = WaypointTextMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
                if (resetEmailSent) {
                    Text(
                        text = stringResource(R.string.auth_reset_sent),
                        color = WaypointTerracotta,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            } else {
                LabeledField(
                    label = stringResource(R.string.auth_password_label),
                    value = password,
                    onValueChange = { password = it },
                    hint = stringResource(R.string.auth_password_hint),
                    isPassword = true,
                )
            }

            if (mode == EmailAuthMode.SIGN_UP) {
                LabeledField(
                    label = stringResource(R.string.auth_confirm_password_label),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    hint = stringResource(R.string.auth_password_hint),
                    isPassword = true,
                )
            }

            if (mode == EmailAuthMode.SIGN_IN) {
                TextButton(
                    onClick = { mode = EmailAuthMode.RESET },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(stringResource(R.string.auth_forgot_password_cta), color = WaypointTerracotta, fontSize = 13.sp)
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = WaypointTerracotta,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }

            Button(
                onClick = {
                    onSubmit(
                        when (mode) {
                            EmailAuthMode.SIGN_UP -> EmailAuthSubmission.SignUp(displayName, email, password, confirmPassword)
                            EmailAuthMode.SIGN_IN -> EmailAuthSubmission.SignIn(email, password)
                            EmailAuthMode.RESET -> EmailAuthSubmission.Reset(email)
                        },
                    )
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = WaypointTerracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 2.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(ctaFor(mode)))
                }
            }

            if (mode != EmailAuthMode.RESET) {
                TextButton(
                    onClick = { mode = if (mode == EmailAuthMode.SIGN_IN) EmailAuthMode.SIGN_UP else EmailAuthMode.SIGN_IN },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(if (mode == EmailAuthMode.SIGN_IN) R.string.auth_switch_to_signup else R.string.auth_switch_to_signin),
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                    )
                }
            } else {
                TextButton(onClick = { mode = EmailAuthMode.SIGN_IN }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.auth_switch_to_signin), color = WaypointTextMuted, fontSize = 13.sp)
                }
            }
        }
    }
}

private fun titleFor(mode: EmailAuthMode) = when (mode) {
    EmailAuthMode.SIGN_UP -> R.string.auth_email_signup_title
    EmailAuthMode.SIGN_IN -> R.string.auth_email_signin_title
    EmailAuthMode.RESET -> R.string.auth_reset_title
}

private fun ctaFor(mode: EmailAuthMode) = when (mode) {
    EmailAuthMode.SIGN_UP -> R.string.auth_signup_cta
    EmailAuthMode.SIGN_IN -> R.string.auth_signin_cta
    EmailAuthMode.RESET -> R.string.auth_reset_cta
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Text(
        text = label,
        color = WaypointTextMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp),
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = WaypointTerracotta),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
    )
}
