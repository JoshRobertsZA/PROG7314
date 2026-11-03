package com.waypoint.app.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waypoint.app.R
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSave: (displayName: String) -> Unit,
    modifier: Modifier = Modifier,
    initialDisplayName: String = "",
    photoUrl: String = "",
    isSaving: Boolean = false,
    errorMessage: String? = null,
) {
    var displayName by remember { mutableStateOf(initialDisplayName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp)) {
            Text(
                text = stringResource(R.string.profile_edit_screen_title),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
            CircleIconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart),
                size = 40.dp,
                fillColor = WaypointTerracotta,
                borderColor = null,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = stringResource(R.string.auth_back_cd),
                    tint = WaypointCard,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            AsyncImage(
                model = photoUrl.ifBlank { null },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_mock_avatar),
                error = painterResource(R.drawable.img_mock_avatar),
                fallback = painterResource(R.drawable.img_mock_avatar),
                modifier = Modifier
                    .size(88.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
            )

            Text(
                text = stringResource(R.string.auth_display_name_label),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                placeholder = { Text(stringResource(R.string.auth_display_name_hint)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = WaypointTerracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )

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
                onClick = { onSave(displayName) },
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = WaypointTerracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 2.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.profile_edit_save_cta))
                }
            }
        }
    }
}
