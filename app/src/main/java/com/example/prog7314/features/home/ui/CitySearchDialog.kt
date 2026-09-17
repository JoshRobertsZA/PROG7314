package com.example.prog7314.features.home.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.features.home.data.WikipediaCitySearch
import kotlinx.coroutines.delay

/**
 * Dialog that lets the user search for a city via the Wikipedia OpenSearch
 * API. LaunchedEffect(query) handles debouncing natively: it cancels the
 * previous coroutine whenever query changes, so the delay(300) effectively
 * debounces without needing a separate scope or Job reference.
 */
@Composable
fun CitySearchDialog(
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchAttempted by remember { mutableStateOf(false) }

    // LaunchedEffect cancels and restarts on every query change, giving us
    // free debouncing: the delay(300) is interrupted on each keystroke.
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
                    text = "Select a city",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = WaypointTextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Type a city name...") },
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
                        text = "No results found",
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
