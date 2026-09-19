// declares that this file belongs to the package `com.waypoint.app.features.currencyexchange.ui`
package com.waypoint.app.features.currencyexchange.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
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
// imports `androidx.compose.material3.Surface` for use in this file
import androidx.compose.material3.Surface
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.SelectionModal` for use in this file
import com.waypoint.app.core.common.SelectionModal
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary

// declares private read-only property `SUPPORTED_CURRENCIES`, initialised with the result of calling `listOf(…)`
private val SUPPORTED_CURRENCIES = listOf(
    // continues the statement started above: `"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",`
    "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",
    // continues the statement started above: `"INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",`
    "INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",
    // continues the statement started above: `"NZD", "KES", "NGN", "EGP",`
    "NZD", "KES", "NGN", "EGP",
// closes the multi-line argument list started above
)

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun CurrencyExchangeModal(`
fun CurrencyExchangeModal(
    // continues the statement started above: `onSaveClick: () -> Unit,`
    onSaveClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `fromCode: String = "USD",`
    fromCode: String = "USD",
    // continues the statement started above: `rate: Double? = null,`
    rate: Double? = null,
    // continues the statement started above: `isLoading: Boolean = false,`
    isLoading: Boolean = false,
    // continues the statement started above: `onFromCodeChanged: (String) -> Unit = {},`
    onFromCodeChanged: (String) -> Unit = {},
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `showCurrencyPicker`, delegated to `remember { mutableStateOf(false) }`
    var showCurrencyPicker by remember { mutableStateOf(false) }

    // `if` statement: the block below runs when `showCurrencyPicker` is true
    if (showCurrencyPicker) {
        // calls `Dialog` with arguments `(onDismissRequest = { showCurrencyPicker = fa…)` and opens a trailing lambda / block
        Dialog(onDismissRequest = { showCurrencyPicker = false }) {
            // calls `Surface` with arguments `(shape = RoundedCornerShape(16.dp), tonalElev…)` and opens a trailing lambda / block
            Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 4.dp) {
                // calls `Column` with arguments `(modifier = Modifier.padding(16.dp))` and opens a trailing lambda / block
                Column(modifier = Modifier.padding(16.dp)) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.currency_modal_select),`
                        text = stringResource(R.string.currency_modal_select),
                        // continues the statement started above: `fontSize = 16.sp,`
                        fontSize = 16.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color = WaypointTextPrimary,
                        // continues the statement started above: `modifier = Modifier.padding(bottom = 8.dp),`
                        modifier = Modifier.padding(bottom = 8.dp),
                    // closes the multi-line argument list started above
                    )
                    // opens a block after `LazyColumn`
                    LazyColumn {
                        // calls `items` with arguments `(SUPPORTED_CURRENCIES)`
                        items(SUPPORTED_CURRENCIES) { code ->
                            // continues the statement started above: `Text(`
                            Text(
                                // continues the statement started above: `text = code,`
                                text = code,
                                // continues the statement started above: `color = if (code == fromCode) WaypointTerracotta else Waypo…`
                                color = if (code == fromCode) WaypointTerracotta else WaypointTextPrimary,
                                // continues the statement started above: `fontWeight = if (code == fromCode) FontWeight.Bold else Fon…`
                                fontWeight = if (code == fromCode) FontWeight.Bold else FontWeight.Normal,
                                // continues the statement started above: `fontSize = 15.sp,`
                                fontSize = 15.sp,
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.fillMaxWidth()`
                                    .fillMaxWidth()
                                    // continues the statement started above: `.clickable {`
                                    .clickable {
                                        // calls `onFromCodeChanged` with arguments `(code)`
                                        onFromCodeChanged(code)
                                        // assigns `showCurrencyPicker` the value `false`
                                        showCurrencyPicker = false
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
            // closes the lambda passed to `Surface`
            }
        // closes the lambda passed to `Dialog`
        }
    // closes the if block
    }

    // calls `SelectionModal` with an argument list that continues on the following lines
    SelectionModal(
        // continues the statement started above: `title = stringResource(R.string.currency_modal_title),`
        title = stringResource(R.string.currency_modal_title),
        // continues the statement started above: `onSaveClick = onSaveClick,`
        onSaveClick = onSaveClick,
        // continues the statement started above: `modifier = modifier,`
        modifier = modifier,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth(), vertical…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.weight(1f)`
                    .weight(1f)
                    // continues the statement started above: `.clickable { showCurrencyPicker = true },`
                    .clickable { showCurrencyPicker = true },
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `CurrencyBox` with an argument list that continues on the following lines
                CurrencyBox(
                    // continues the statement started above: `code = fromCode,`
                    code = fromCode,
                    // continues the statement started above: `rate = "1.00",`
                    rate = "1.00",
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                    // continues the statement started above: `isSelected = true,`
                    isSelected = true,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.currency_modal_arrow_glyph),`
                text = stringResource(R.string.currency_modal_arrow_glyph),
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `fontSize = 24.sp,`
                fontSize = 24.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.padding(horizontal = 12.dp),`
                modifier = Modifier.padding(horizontal = 12.dp),
            // closes the multi-line argument list started above
            )
            // calls `Box` with arguments `(modifier = Modifier.weight(1f))` and opens a trailing lambda / block
            Box(modifier = Modifier.weight(1f)) {
                // `if` statement: the block below runs when `isLoading` is true
                if (isLoading) {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.background(WaypointCream, RoundedCornerShape(RadiusButton))`
                            .background(WaypointCream, RoundedCornerShape(RadiusButton))
                            // continues the statement started above: `.border(1.5.dp, WaypointBorderSoft, RoundedCornerShape(Radi…`
                            .border(1.5.dp, WaypointBorderSoft, RoundedCornerShape(RadiusButton))
                            // continues the statement started above: `.padding(14.dp),`
                            .padding(14.dp),
                        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                        horizontalAlignment = Alignment.CenterHorizontally,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with arguments `("ZAR", color = WaypointTextPrimary, fontSize…)`
                        Text("ZAR", color = WaypointTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                        CircularProgressIndicator(
                            // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
                            modifier = Modifier.padding(top = 4.dp),
                            // continues the statement started above: `color = WaypointTerracotta,`
                            color = WaypointTerracotta,
                            // continues the statement started above: `strokeWidth = 2.dp,`
                            strokeWidth = 2.dp,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `CurrencyBox` with an argument list that continues on the following lines
                    CurrencyBox(
                        // continues the statement started above: `code = "ZAR",`
                        code = "ZAR",
                        // continues the statement started above: `rate = rate?.let { "%.2f".format(it) } ?: "–",`
                        rate = rate?.let { "%.2f".format(it) } ?: "–",
                        // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                        modifier = Modifier.fillMaxWidth(),
                    // closes the multi-line argument list started above
                    )
                // closes the else branch
                }
            // closes the lambda passed to `Box`
            }
        // closes the lambda passed to `Row`
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun CurrencyBox(`
private fun CurrencyBox(
    // continues the statement started above: `code: String,`
    code: String,
    // continues the statement started above: `rate: String,`
    rate: String,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `isSelected: Boolean = false,`
    isSelected: Boolean = false,
// ends the argument list started above and opens the block that follows
) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.background(WaypointCream, RoundedCornerShape(RadiusButton))`
            .background(WaypointCream, RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.border(`
            .border(
                // continues the statement started above: `width = if (isSelected) 2.dp else 1.5.dp,`
                width = if (isSelected) 2.dp else 1.5.dp,
                // continues the statement started above: `color = if (isSelected) WaypointTerracotta else WaypointBor…`
                color = if (isSelected) WaypointTerracotta else WaypointBorderSoft,
                // continues the statement started above: `shape = RoundedCornerShape(RadiusButton),`
                shape = RoundedCornerShape(RadiusButton),
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.padding(14.dp),`
            .padding(14.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(code, color = WaypointTextPrimary, fontSize …)`
        Text(code, color = WaypointTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        // calls `Text` with arguments `(rate, color = WaypointTextMuted, fontSize = …)`
        Text(rate, color = WaypointTextMuted, fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
    // closes the block
    }
// closes the block
}
