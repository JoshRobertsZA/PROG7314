package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta

// Round icon button used for back and close actions.
//Pass a contentDescription so TalkBack says what the button does instead of reading the glyph.

@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 36.dp,
    fillColor: Color = WaypointCard,
    borderColor: Color? = WaypointTerracotta,
    borderWidth: Dp = 1.2.dp,
    content: @Composable () -> Unit,
) {
    // Replaces the inner glyph/icon semantics with the description, and keeps the button clickable for TalkBack
    val a11y = if (contentDescription != null) {
        Modifier.clearAndSetSemantics {
            this.contentDescription = contentDescription
            role = Role.Button
            this.onClick(label = null, action = { onClick(); true })
        }
    } else Modifier

    var box: Modifier = modifier
        // Keeps a 48dp touch area even though the circle is drawn smaller
        .minimumInteractiveComponentSize()
        .then(a11y)
        .size(size)
        .clickable(onClick = onClick, role = Role.Button)
        .background(color = fillColor, shape = CircleShape)
    if (borderColor != null) {
        box = box.border(width = borderWidth, color = borderColor, shape = CircleShape)
    }
    Box(modifier = box, contentAlignment = Alignment.Center) {
        content()
    }
}