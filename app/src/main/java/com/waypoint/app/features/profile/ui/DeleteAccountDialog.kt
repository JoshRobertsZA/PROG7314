package com.waypoint.app.features.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.waypoint.app.R
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirmWithPassword: (String) -> Unit,
    onConfirmWithGoogle: () -> Unit,
    modifier: Modifier = Modifier,
    canDelete: Boolean = true,
    requiresPassword: Boolean = true,
    isDeleting: Boolean = false,
    errorMessage: String? = null,
) {
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), tonalElevation = 4.dp) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.profile_delete_dialog_title),
                    color = WaypointTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.profile_delete_dialog_warning),
                    color = WaypointTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                )

                if (!canDelete) {
                    Text(
                        text = stringResource(R.string.profile_delete_reauth_required),
                        color = WaypointTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                } else {
                    if (requiresPassword) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text(stringResource(R.string.auth_password_hint)) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        )
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
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss, enabled = !isDeleting) {
                        Text(stringResource(R.string.profile_delete_cancel_cta))
                    }
                    if (canDelete) {
                        Button(
                            onClick = { if (requiresPassword) onConfirmWithPassword(password) else onConfirmWithGoogle() },
                            enabled = !isDeleting,
                            colors = ButtonDefaults.buttonColors(containerColor = WaypointTerracotta),
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            if (isDeleting) {
                                CircularProgressIndicator(modifier = Modifier.padding(vertical = 2.dp), strokeWidth = 2.dp)
                            } else {
                                Text(
                                    stringResource(
                                        if (requiresPassword) R.string.profile_delete_confirm_cta else R.string.profile_delete_confirm_google_cta,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
