package com.waypoint.app.features.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.waypoint.app.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.features.home.data.WikipediaCitySearch
import kotlinx.coroutines.delay

/**
 * Wikipedia-backed city search, used to pick the weather city on Home, the area in Explore, and a trip's destination.
 * With [allowCustomEntry] the typed text can be used as is, so a trip destination can still be saved offline.
 */
@Composable
fun CitySearchDialog(
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit,
    allowCustomEntry: Boolean = false,
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchAttempted by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length < 2) {
            suggestions = emptyList()
            isSearching = false
            searchAttempted = false
            return@LaunchedEffect
        }
        delay(300)
        isSearching = true
        searchAttempted = false
        val results = WikipediaCitySearch.search(query)
        suggestions = results
        isSearching = false
        searchAttempted = true
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.city_search_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = WaypointTextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text(stringResource(R.string.city_search_hint)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (isSearching) {
                    CircularProgressIndicator(
                        color = WaypointTerracotta,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(24.dp),
                    )
                } else if (searchAttempted && suggestions.isEmpty()) {
                    Text(
                        text = stringResource(R.string.city_search_no_results),
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    val typed = query.trim()
                    // Offered whenever something is typed and it is not already one of the suggestions
                    if (allowCustomEntry && typed.length >= 2 && suggestions.none { it.equals(typed, ignoreCase = true) }) {
                        item {
                            Text(
                                text = stringResource(R.string.city_search_use_typed, typed),
                                color = WaypointTerracotta,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCitySelected(typed)
                                        onDismiss()
                                    }
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                            )
                            HorizontalDivider(color = WaypointTextMuted.copy(alpha = 0.15f))
                        }
                    }
                    items(suggestions) { city ->
                        Text(
                            text = city,
                            color = WaypointTextPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCitySelected(city)
                                    onDismiss()
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                        )
                        HorizontalDivider(color = WaypointTextMuted.copy(alpha = 0.15f))
                    }
                }
            }
        }
    }
}
