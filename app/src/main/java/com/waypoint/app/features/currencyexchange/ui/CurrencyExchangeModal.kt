package com.waypoint.app.features.currencyexchange.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.common.SelectionModal
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

data class CurrencyInfo(val code: String, val flag: String, val name: String)

fun currencyInfo(code: String): CurrencyInfo = when (code.uppercase()) {
    "USD" -> CurrencyInfo("USD", "🇺🇸", "US Dollar")
    "ZAR" -> CurrencyInfo("ZAR", "🇿🇦", "South African Rand")
    "EUR" -> CurrencyInfo("EUR", "🇪🇺", "Euro")
    "GBP" -> CurrencyInfo("GBP", "🇬🇧", "British Pound")
    "JPY" -> CurrencyInfo("JPY", "🇯🇵", "Japanese Yen")
    "AUD" -> CurrencyInfo("AUD", "🇦🇺", "Australian Dollar")
    "CAD" -> CurrencyInfo("CAD", "🇨🇦", "Canadian Dollar")
    "CHF" -> CurrencyInfo("CHF", "🇨🇭", "Swiss Franc")
    "CNY" -> CurrencyInfo("CNY", "🇨🇳", "Chinese Yuan")
    "INR" -> CurrencyInfo("INR", "🇮🇳", "Indian Rupee")
    "BRL" -> CurrencyInfo("BRL", "🇧🇷", "Brazilian Real")
    "MXN" -> CurrencyInfo("MXN", "🇲🇽", "Mexican Peso")
    "SGD" -> CurrencyInfo("SGD", "🇸🇬", "Singapore Dollar")
    "HKD" -> CurrencyInfo("HKD", "🇭🇰", "Hong Kong Dollar")
    "NOK" -> CurrencyInfo("NOK", "🇳🇴", "Norwegian Krone")
    "SEK" -> CurrencyInfo("SEK", "🇸🇪", "Swedish Krona")
    "DKK" -> CurrencyInfo("DKK", "🇩🇰", "Danish Krone")
    "NZD" -> CurrencyInfo("NZD", "🇳🇿", "New Zealand Dollar")
    "KES" -> CurrencyInfo("KES", "🇰🇪", "Kenyan Shilling")
    "NGN" -> CurrencyInfo("NGN", "🇳🇬", "Nigerian Naira")
    "EGP" -> CurrencyInfo("EGP", "🇪🇬", "Egyptian Pound")
    else -> CurrencyInfo(code, "💱", code)
}

private val SUPPORTED_CURRENCIES = listOf(
    "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",
    "INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",
    "NZD", "KES", "NGN", "EGP"
)

@Composable
fun CurrencyExchangeModal(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    fromCode: String = "USD",
    rate: Double? = null,
    isLoading: Boolean = false,
    onFromCodeChanged: (String) -> Unit = {},
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf("35") }

    val fromInfo = currencyInfo(fromCode)
    val toInfo = currencyInfo("ZAR")

    val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
    val convertedResult = rate?.let { parsedAmount * it }

    SelectionModal(
        title = stringResource(R.string.currency_modal_title),
        onSaveClick = onSaveClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WaypointCream, RoundedCornerShape(20.dp))
                .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                .padding(18.dp),
        ) {
            // ROW 1: Source Currency & Amount Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Flag emoji directly without white background circle
                Text(
                    text = fromInfo.flag,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 8.dp),
                )

                // Middle: "Amount" + Input Field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Amount",
                        fontSize = 12.sp,
                        color = WaypointTextMuted,
                        fontWeight = FontWeight.Medium,
                    )
                    BasicTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { c -> c.isDigit() || c == '.' } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextPrimary,
                        ),
                    )
                }

                // Currency Selector Pill ("USD ∨") with anchored DropdownMenu
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(WaypointCard)
                            .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = fromInfo.code,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextPrimary,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "∨",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextMuted,
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .background(WaypointCard)
                            .heightIn(max = 260.dp),
                    ) {
                        SUPPORTED_CURRENCIES.forEach { code ->
                            val info = currencyInfo(code)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(info.flag, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${info.code} - ${info.name}",
                                            fontSize = 13.sp,
                                            fontWeight = if (code == fromCode) FontWeight.Bold else FontWeight.Normal,
                                            color = if (code == fromCode) WaypointTerracotta else WaypointTextPrimary,
                                        )
                                    }
                                },
                                onClick = {
                                    onFromCodeChanged(code)
                                    dropdownExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            // DIVIDER WITH FLOATING SWAP ICON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                HorizontalDivider(
                    color = WaypointBorderSoft.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth(),
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(WaypointTerracotta),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("⇄", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ROW 2: Target Currency & Converted Result
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Flag emoji directly without white background circle
                Text(
                    text = toInfo.flag,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 8.dp),
                )

                // Middle: "Convert To" + Converted Value Text
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Convert To",
                        fontSize = 12.sp,
                        color = WaypointTextMuted,
                        fontWeight = FontWeight.Medium,
                    )
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(18.dp),
                            color = WaypointTerracotta,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = convertedResult?.let { "%.2f".format(it) } ?: "–",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextPrimary,
                        )
                    }
                }

                // Target Currency Pill ("ZAR")
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WaypointCard)
                        .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = toInfo.code,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WaypointTextPrimary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // BOTTOM ROW: Market Rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Market Rate",
                    fontSize = 13.sp,
                    color = WaypointTextMuted,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = rate?.let { "1 ${fromInfo.code} = ${"%.2f".format(it)} ${toInfo.code}" } ?: "Loading rate...",
                    fontSize = 13.sp,
                    color = WaypointTerracotta,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
