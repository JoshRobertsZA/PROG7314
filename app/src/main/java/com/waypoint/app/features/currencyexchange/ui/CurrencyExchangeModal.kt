// declares that this file belongs to the package `com.waypoint.app.features.currencyexchange.ui`
package com.waypoint.app.features.currencyexchange.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.heightIn` for use in this file
import androidx.compose.foundation.layout.heightIn
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.text.BasicTextField` for use in this file
import androidx.compose.foundation.text.BasicTextField
// imports `androidx.compose.foundation.text.KeyboardOptions` for use in this file
import androidx.compose.foundation.text.KeyboardOptions
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.DropdownMenu` for use in this file
import androidx.compose.material3.DropdownMenu
// imports `androidx.compose.material3.DropdownMenuItem` for use in this file
import androidx.compose.material3.DropdownMenuItem
// imports `androidx.compose.material3.HorizontalDivider` for use in this file
import androidx.compose.material3.HorizontalDivider
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
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.TextStyle` for use in this file
import androidx.compose.ui.text.TextStyle
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.input.KeyboardType` for use in this file
import androidx.compose.ui.text.input.KeyboardType
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.SelectionModal` for use in this file
import com.waypoint.app.core.common.SelectionModal
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// declares data class `CurrencyInfo` with a primary constructor taking 3 parameters (`code`, `flag`, `name`)
data class CurrencyInfo(val code: String, val flag: String, val name: String)

// declares function `currencyInfo` taking 1 parameter (`code`), returning `CurrencyInfo`; its body is the expression `when (code.uppercase()) {`
fun currencyInfo(code: String): CurrencyInfo = when (code.uppercase()) {
    // lambda `"USD" -> CurrencyInfo("USD", "🇺🇸", "US…`
    "USD" -> CurrencyInfo("USD", "🇺🇸", "US Dollar")
    // lambda `"ZAR" -> CurrencyInfo("ZAR", "🇿🇦", "So…`
    "ZAR" -> CurrencyInfo("ZAR", "🇿🇦", "South African Rand")
    // lambda `"EUR" -> CurrencyInfo("EUR", "🇪🇺", "Eu…`
    "EUR" -> CurrencyInfo("EUR", "🇪🇺", "Euro")
    // lambda `"GBP" -> CurrencyInfo("GBP", "🇬🇧", "Br…`
    "GBP" -> CurrencyInfo("GBP", "🇬🇧", "British Pound")
    // lambda `"JPY" -> CurrencyInfo("JPY", "🇯🇵", "Ja…`
    "JPY" -> CurrencyInfo("JPY", "🇯🇵", "Japanese Yen")
    // lambda `"AUD" -> CurrencyInfo("AUD", "🇦🇺", "Au…`
    "AUD" -> CurrencyInfo("AUD", "🇦🇺", "Australian Dollar")
    // lambda `"CAD" -> CurrencyInfo("CAD", "🇨🇦", "Ca…`
    "CAD" -> CurrencyInfo("CAD", "🇨🇦", "Canadian Dollar")
    // lambda `"CHF" -> CurrencyInfo("CHF", "🇨🇭", "Sw…`
    "CHF" -> CurrencyInfo("CHF", "🇨🇭", "Swiss Franc")
    // lambda `"CNY" -> CurrencyInfo("CNY", "🇨🇳", "Ch…`
    "CNY" -> CurrencyInfo("CNY", "🇨🇳", "Chinese Yuan")
    // lambda `"INR" -> CurrencyInfo("INR", "🇮🇳", "In…`
    "INR" -> CurrencyInfo("INR", "🇮🇳", "Indian Rupee")
    // lambda `"BRL" -> CurrencyInfo("BRL", "🇧🇷", "Br…`
    "BRL" -> CurrencyInfo("BRL", "🇧🇷", "Brazilian Real")
    // lambda `"MXN" -> CurrencyInfo("MXN", "🇲🇽", "Me…`
    "MXN" -> CurrencyInfo("MXN", "🇲🇽", "Mexican Peso")
    // lambda `"SGD" -> CurrencyInfo("SGD", "🇸🇬", "Si…`
    "SGD" -> CurrencyInfo("SGD", "🇸🇬", "Singapore Dollar")
    // lambda `"HKD" -> CurrencyInfo("HKD", "🇭🇰", "Ho…`
    "HKD" -> CurrencyInfo("HKD", "🇭🇰", "Hong Kong Dollar")
    // lambda `"NOK" -> CurrencyInfo("NOK", "🇳🇴", "No…`
    "NOK" -> CurrencyInfo("NOK", "🇳🇴", "Norwegian Krone")
    // lambda `"SEK" -> CurrencyInfo("SEK", "🇸🇪", "Sw…`
    "SEK" -> CurrencyInfo("SEK", "🇸🇪", "Swedish Krona")
    // lambda `"DKK" -> CurrencyInfo("DKK", "🇩🇰", "Da…`
    "DKK" -> CurrencyInfo("DKK", "🇩🇰", "Danish Krone")
    // lambda `"NZD" -> CurrencyInfo("NZD", "🇳🇿", "Ne…`
    "NZD" -> CurrencyInfo("NZD", "🇳🇿", "New Zealand Dollar")
    // lambda `"KES" -> CurrencyInfo("KES", "🇰🇪", "Ke…`
    "KES" -> CurrencyInfo("KES", "🇰🇪", "Kenyan Shilling")
    // lambda `"NGN" -> CurrencyInfo("NGN", "🇳🇬", "Ni…`
    "NGN" -> CurrencyInfo("NGN", "🇳🇬", "Nigerian Naira")
    // lambda `"EGP" -> CurrencyInfo("EGP", "🇪🇬", "Eg…`
    "EGP" -> CurrencyInfo("EGP", "🇪🇬", "Egyptian Pound")
    // `else` branch of the `when`: evaluates `CurrencyInfo(code, "💱", code)`
    else -> CurrencyInfo(code, "💱", code)
// closes the block
}

// declares private read-only property `SUPPORTED_CURRENCIES`, initialised with the result of calling `listOf(…)`
private val SUPPORTED_CURRENCIES = listOf(
    // continues the statement started above: `"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",`
    "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",
    // continues the statement started above: `"INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",`
    "INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",
    // continues the statement started above: `"NZD", "KES", "NGN", "EGP"`
    "NZD", "KES", "NGN", "EGP"
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
    // continues the statement started above: `initialAmount: String = "1",`
    initialAmount: String = "1",
    // continues the statement started above: `rate: Double? = null,`
    rate: Double? = null,
    // continues the statement started above: `isLoading: Boolean = false,`
    isLoading: Boolean = false,
    // continues the statement started above: `onFromCodeChanged: (String) -> Unit = {},`
    onFromCodeChanged: (String) -> Unit = {},
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `dropdownExpanded`, delegated to `remember { mutableStateOf(false) }`
    var dropdownExpanded by remember { mutableStateOf(false) }
    // declares mutable property `amountText`, delegated to `remember { mutableStateOf(initialAmount…`
    var amountText by remember { mutableStateOf(initialAmount) }

    // declares read-only property `fromInfo`, initialised with the result of calling `currencyInfo(…)`
    val fromInfo = currencyInfo(fromCode)
    // declares read-only property `toInfo`, initialised with the result of calling `currencyInfo(…)`
    val toInfo = currencyInfo("ZAR")

    // declares read-only property `parsedAmount`, initialised with the result of calling `amountText.toDoubleOrNull(…)`
    val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
    // declares read-only property `convertedResult`, initialised to `rate?.let { parsedAmount * it }`
    val convertedResult = rate?.let { parsedAmount * it }

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
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.background(WaypointCream, RoundedCornerShape(20.dp))`
                .background(WaypointCream, RoundedCornerShape(20.dp))
                // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))`
                .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                // continues the statement started above: `.padding(18.dp),`
                .padding(18.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                modifier = Modifier.fillMaxWidth(),
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
                // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
                horizontalArrangement = Arrangement.SpaceBetween,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = fromInfo.flag,`
                    text = fromInfo.flag,
                    // continues the statement started above: `fontSize = 32.sp,`
                    fontSize = 32.sp,
                    // continues the statement started above: `modifier = Modifier.padding(end = 8.dp),`
                    modifier = Modifier.padding(end = 8.dp),
                // closes the multi-line argument list started above
                )

                // calls `Column` with arguments `(modifier = Modifier.weight(1f))` and opens a trailing lambda / block
                Column(modifier = Modifier.weight(1f)) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = "Amount",`
                        text = "Amount",
                        // continues the statement started above: `fontSize = 12.sp,`
                        fontSize = 12.sp,
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontWeight = FontWeight.Medium,`
                        fontWeight = FontWeight.Medium,
                    // closes the multi-line argument list started above
                    )
                    // calls `BasicTextField` with an argument list that continues on the following lines
                    BasicTextField(
                        // continues the statement started above: `value = amountText,`
                        value = amountText,
                        // continues the statement started above: `onValueChange = { amountText = it.filter { c -> c.isDigit()…`
                        onValueChange = { amountText = it.filter { c -> c.isDigit() || c == '.' } },
                        // continues the statement started above: `keyboardOptions = KeyboardOptions(keyboardType = KeyboardTy…`
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        // continues the statement started above: `singleLine = true,`
                        singleLine = true,
                        // continues the statement started above: `textStyle = TextStyle(`
                        textStyle = TextStyle(
                            // continues the statement started above: `fontSize = 20.sp,`
                            fontSize = 20.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color = WaypointTextPrimary,
                        // closes the multi-line argument list started above
                        ),
                    // closes the multi-line argument list started above
                    )
                // closes the lambda passed to `Column`
                }

                // opens a block after `Box`
                Box {
                    // calls `Row` with an argument list that continues on the following lines
                    Row(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.clip(RoundedCornerShape(20.dp))`
                            .clip(RoundedCornerShape(20.dp))
                            // continues the statement started above: `.background(WaypointCard)`
                            .background(WaypointCard)
                            // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))`
                            .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                            // continues the statement started above: `.clickable { dropdownExpanded = true }`
                            .clickable { dropdownExpanded = true }
                            // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 8.dp),`
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                        verticalAlignment = Alignment.CenterVertically,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = fromInfo.code,`
                            text = fromInfo.code,
                            // continues the statement started above: `fontSize = 14.sp,`
                            fontSize = 14.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color = WaypointTextPrimary,
                        // closes the multi-line argument list started above
                        )
                        // calls `Spacer` with arguments `(modifier = Modifier.width(4.dp))`
                        Spacer(modifier = Modifier.width(4.dp))
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = "∨",`
                            text = "∨",
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }

                    // calls `DropdownMenu` with an argument list that continues on the following lines
                    DropdownMenu(
                        // continues the statement started above: `expanded = dropdownExpanded,`
                        expanded = dropdownExpanded,
                        // continues the statement started above: `onDismissRequest = { dropdownExpanded = false },`
                        onDismissRequest = { dropdownExpanded = false },
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.background(WaypointCard)`
                            .background(WaypointCard)
                            // continues the statement started above: `.heightIn(max = 260.dp),`
                            .heightIn(max = 260.dp),
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // expression: `SUPPORTED_CURRENCIES.forEach { code ->`
                        SUPPORTED_CURRENCIES.forEach { code ->
                            // continues the statement started above: `val info = currencyInfo(code)`
                            val info = currencyInfo(code)
                            // calls `DropdownMenuItem` with an argument list that continues on the following lines
                            DropdownMenuItem(
                                // continues the statement started above: `text = {`
                                text = {
                                    // calls `Row` with arguments `(verticalAlignment = Alignment.CenterVertical…)` and opens a trailing lambda / block
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // calls `Text` with arguments `(info.flag, fontSize = 18.sp)`
                                        Text(info.flag, fontSize = 18.sp)
                                        // calls `Spacer` with arguments `(modifier = Modifier.width(8.dp))`
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // calls `Text` with an argument list that continues on the following lines
                                        Text(
                                            // continues the statement started above: `text = "${info.code} - ${info.name}",`
                                            text = "${info.code} - ${info.name}",
                                            // continues the statement started above: `fontSize = 13.sp,`
                                            fontSize = 13.sp,
                                            // continues the statement started above: `fontWeight = if (code == fromCode) FontWeight.Bold else Fon…`
                                            fontWeight = if (code == fromCode) FontWeight.Bold else FontWeight.Normal,
                                            // continues the statement started above: `color = if (code == fromCode) WaypointTerracotta else Waypo…`
                                            color = if (code == fromCode) WaypointTerracotta else WaypointTextPrimary,
                                        // closes the multi-line argument list started above
                                        )
                                    // closes the lambda passed to `Row`
                                    }
                                // closes the block
                                },
                                // continues the statement started above: `onClick = {`
                                onClick = {
                                    // calls `onFromCodeChanged` with arguments `(code)`
                                    onFromCodeChanged(code)
                                    // assigns `dropdownExpanded` the value `false`
                                    dropdownExpanded = false
                                // closes the block
                                },
                            // closes the multi-line argument list started above
                            )
                        // closes the block
                        }
                    // closes the block
                    }
                // closes the block
                }
            // closes the block
            }

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(vertical = 12.dp),`
                    .padding(vertical = 12.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `HorizontalDivider` with an argument list that continues on the following lines
                HorizontalDivider(
                    // continues the statement started above: `color = WaypointBorderSoft.copy(alpha = 0.6f),`
                    color = WaypointBorderSoft.copy(alpha = 0.6f),
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                // closes the multi-line argument list started above
                )
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.size(36.dp)`
                        .size(36.dp)
                        // continues the statement started above: `.clip(CircleShape)`
                        .clip(CircleShape)
                        // continues the statement started above: `.background(WaypointTerracotta),`
                        .background(WaypointTerracotta),
                    // continues the statement started above: `contentAlignment = Alignment.Center,`
                    contentAlignment = Alignment.Center,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with arguments `("⇄", color = White, fontSize = 18.sp, fontWe…)`
                    Text("⇄", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                // closes the block
                }
            // closes the block
            }

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                modifier = Modifier.fillMaxWidth(),
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
                // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
                horizontalArrangement = Arrangement.SpaceBetween,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = toInfo.flag,`
                    text = toInfo.flag,
                    // continues the statement started above: `fontSize = 32.sp,`
                    fontSize = 32.sp,
                    // continues the statement started above: `modifier = Modifier.padding(end = 8.dp),`
                    modifier = Modifier.padding(end = 8.dp),
                // closes the multi-line argument list started above
                )

                // calls `Column` with arguments `(modifier = Modifier.weight(1f))` and opens a trailing lambda / block
                Column(modifier = Modifier.weight(1f)) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = "Convert To",`
                        text = "Convert To",
                        // continues the statement started above: `fontSize = 12.sp,`
                        fontSize = 12.sp,
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontWeight = FontWeight.Medium,`
                        fontWeight = FontWeight.Medium,
                    // closes the multi-line argument list started above
                    )
                    // `if` statement: the block below runs when `isLoading` is true
                    if (isLoading) {
                        // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                        CircularProgressIndicator(
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.padding(top = 2.dp)`
                                .padding(top = 2.dp)
                                // continues the statement started above: `.size(18.dp),`
                                .size(18.dp),
                            // continues the statement started above: `color = WaypointTerracotta,`
                            color = WaypointTerracotta,
                            // continues the statement started above: `strokeWidth = 2.dp,`
                            strokeWidth = 2.dp,
                        // closes the multi-line argument list started above
                        )
                    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                    } else {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = convertedResult?.let { "%.2f".format(it) } ?: "–",`
                            text = convertedResult?.let { "%.2f".format(it) } ?: "–",
                            // continues the statement started above: `fontSize = 20.sp,`
                            fontSize = 20.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color = WaypointTextPrimary,
                        // closes the multi-line argument list started above
                        )
                    // closes the else branch
                    }
                // closes the lambda passed to `Column`
                }

                // calls `Row` with an argument list that continues on the following lines
                Row(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.clip(RoundedCornerShape(20.dp))`
                        .clip(RoundedCornerShape(20.dp))
                        // continues the statement started above: `.background(WaypointCard)`
                        .background(WaypointCard)
                        // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))`
                        .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                        // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 8.dp),`
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                    verticalAlignment = Alignment.CenterVertically,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = toInfo.code,`
                        text = toInfo.code,
                        // continues the statement started above: `fontSize = 14.sp,`
                        fontSize = 14.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color = WaypointTextPrimary,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }

            // calls `Spacer` with arguments `(modifier = Modifier.height(18.dp))`
            Spacer(modifier = Modifier.height(18.dp))

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                modifier = Modifier.fillMaxWidth(),
                // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
                horizontalArrangement = Arrangement.SpaceBetween,
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = "Market Rate",`
                    text = "Market Rate",
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontWeight = FontWeight.Medium,`
                    fontWeight = FontWeight.Medium,
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = rate?.let { "1 ${fromInfo.code} = ${"%.2f".format(it…`
                    text = rate?.let { "1 ${fromInfo.code} = ${"%.2f".format(it)} ${toInfo.code}" } ?: "Loading rate...",
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}
