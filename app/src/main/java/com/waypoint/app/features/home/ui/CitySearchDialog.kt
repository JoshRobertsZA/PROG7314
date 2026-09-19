// declares that this file belongs to the package `com.waypoint.app.features.home.ui`
package com.waypoint.app.features.home.ui

// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.lazy.LazyColumn` for use in this file
import androidx.compose.foundation.lazy.LazyColumn
// imports `androidx.compose.foundation.lazy.items` for use in this file
import androidx.compose.foundation.lazy.items
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.HorizontalDivider` for use in this file
import androidx.compose.material3.HorizontalDivider
// imports `androidx.compose.material3.OutlinedTextField` for use in this file
import androidx.compose.material3.OutlinedTextField
// imports `androidx.compose.material3.Surface` for use in this file
import androidx.compose.material3.Surface
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.features.home.data.WikipediaCitySearch` for use in this file
import com.waypoint.app.features.home.data.WikipediaCitySearch
// imports `kotlinx.coroutines.delay` for use in this file
import kotlinx.coroutines.delay

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun CitySearchDialog(`
fun CitySearchDialog(
    // continues the statement started above: `onCitySelected: (String) -> Unit,`
    onCitySelected: (String) -> Unit,
    // continues the statement started above: `onDismiss: () -> Unit,`
    onDismiss: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `query`, delegated to `remember { mutableStateOf("") }`
    var query by remember { mutableStateOf("") }
    // declares mutable property `suggestions`, delegated to `remember { mutableStateOf(emptyList<Str…`
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    // declares mutable property `isSearching`, delegated to `remember { mutableStateOf(false) }`
    var isSearching by remember { mutableStateOf(false) }
    // declares mutable property `searchAttempted`, delegated to `remember { mutableStateOf(false) }`
    var searchAttempted by remember { mutableStateOf(false) }

    // calls `LaunchedEffect` with arguments `(query)` and opens a trailing lambda / block
    LaunchedEffect(query) {
        // `if` statement: the block below runs when `query.length < 2` is true
        if (query.length < 2) {
            // assigns `suggestions` the value `emptyList()`
            suggestions = emptyList()
            // assigns `isSearching` the value `false`
            isSearching = false
            // assigns `searchAttempted` the value `false`
            searchAttempted = false
            // expression: `return@LaunchedEffect`
            return@LaunchedEffect
        // closes the if block
        }
        // calls `delay` with arguments `(300)`
        delay(300)
        // assigns `isSearching` the value `true`
        isSearching = true
        // assigns `searchAttempted` the value `false`
        searchAttempted = false
        // declares read-only property `results`, initialised with the result of calling `WikipediaCitySearch.search(…)`
        val results = WikipediaCitySearch.search(query)
        // assigns `suggestions` the value `results`
        suggestions = results
        // assigns `isSearching` the value `false`
        isSearching = false
        // assigns `searchAttempted` the value `true`
        searchAttempted = true
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `Dialog` with arguments `(onDismissRequest = onDismiss)` and opens a trailing lambda / block
    Dialog(onDismissRequest = onDismiss) {
        // calls `Surface` with an argument list that continues on the following lines
        Surface(
            // continues the statement started above: `shape = RoundedCornerShape(16.dp),`
            shape = RoundedCornerShape(16.dp),
            // continues the statement started above: `tonalElevation = 4.dp,`
            tonalElevation = 4.dp,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Column` with arguments `(modifier = Modifier.padding(16.dp))` and opens a trailing lambda / block
            Column(modifier = Modifier.padding(16.dp)) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.city_search_title),`
                    text = stringResource(R.string.city_search_title),
                    // continues the statement started above: `fontSize = 16.sp,`
                    fontSize = 16.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `modifier = Modifier.padding(bottom = 12.dp),`
                    modifier = Modifier.padding(bottom = 12.dp),
                // closes the multi-line argument list started above
                )
                // calls `OutlinedTextField` with an argument list that continues on the following lines
                OutlinedTextField(
                    // continues the statement started above: `value = query,`
                    value = query,
                    // continues the statement started above: `onValueChange = { query = it },`
                    onValueChange = { query = it },
                    // continues the statement started above: `placeholder = { Text(stringResource(R.string.city_search_hi…`
                    placeholder = { Text(stringResource(R.string.city_search_hint)) },
                    // continues the statement started above: `singleLine = true,`
                    singleLine = true,
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                // closes the multi-line argument list started above
                )
                // `if` statement: the block below runs when `isSearching` is true
                if (isSearching) {
                    // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                    CircularProgressIndicator(
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color = WaypointTerracotta,
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.padding(top = 8.dp)`
                            .padding(top = 8.dp)
                            // continues the statement started above: `.size(24.dp),`
                            .size(24.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens an `else if` branch that runs when `searchAttempted && suggestions.isEmpty()` is true
                } else if (searchAttempted && suggestions.isEmpty()) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.city_search_no_results),`
                        text = stringResource(R.string.city_search_no_results),
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                        modifier = Modifier.padding(top = 8.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the else-if branch
                }
                // calls `LazyColumn` with an argument list that continues on the following lines
                LazyColumn(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.padding(top = 8.dp),`
                        .padding(top = 8.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `items` with arguments `(suggestions)`
                    items(suggestions) { city ->
                        // continues the statement started above: `Text(`
                        Text(
                            // continues the statement started above: `text = city,`
                            text = city,
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color = WaypointTextPrimary,
                            // continues the statement started above: `fontSize = 14.sp,`
                            fontSize = 14.sp,
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.fillMaxWidth()`
                                .fillMaxWidth()
                                // continues the statement started above: `.clickable {`
                                .clickable {
                                    // calls `onCitySelected` with arguments `(city)`
                                    onCitySelected(city)
                                    // calls `onDismiss` with arguments `()`
                                    onDismiss()
                                // closes the block
                                }
                                // expression: `.padding(vertical = 10.dp, horizontal = 4.dp),`
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                        // closes the multi-line argument list started above
                        )
                        // calls `HorizontalDivider` with arguments `(color = WaypointTextMuted.copy(alpha = 0.15f))`
                        HorizontalDivider(color = WaypointTextMuted.copy(alpha = 0.15f))
                    // closes the block
                    }
                // closes the block
                }
            // closes the lambda passed to `Column`
            }
        // closes the block
        }
    // closes the lambda passed to `Dialog`
    }
// closes the block
}
