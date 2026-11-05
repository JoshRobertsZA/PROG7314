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

/** Display details for a currency code. */
data class CurrencyInfo(val code: String, val flag: String, val name: String)

/** Flag and name for a supported currency code. */
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

/** Currencies the converter and the Settings "home currency" picker offer. */
val SUPPORTED_CURRENCIES = listOf(
    "USD", "EUR", "GBP", "ZAR", "JPY", "AUD", "CAD", "CHF", "CNY",
    "INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",
    "NZD", "KES", "NGN", "EGP"
)

/**
 * Currency converter dialog opened from Home: pick two currencies, type an amount, see the converted
 * value.
 */
@Composable
fun CurrencyExchangeModal(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    fromCode: String = "USD",
    toCode: String = "ZAR",
    initialAmount: String = "1",
    rate: Double? = null,
    isLoading: Boolean = false,
    onFromCodeChanged: (String) -> Unit = {},
    onToCodeChanged: (String) -> Unit = {},
    onSwapClick: () -> Unit = {},
) {
    var amountText by remember { mutableStateOf(initialAmount) }

    val fromInfo = currencyInfo(fromCode)
    val toInfo = currencyInfo(toCode)

    val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
    val convertedResult = rate?.let { convertAmount(parsedAmount, it) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = fromInfo.flag,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 8.dp),
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        // Currency modal label from strings.xml
                        text = stringResource(R.string.currency_modal_amount),
                        fontSize = 12.sp,
                        color = WaypointTextMuted,
                        fontWeight = FontWeight.Medium,
                    )
                    BasicTextField(
                        value = amountText,
                        onValueChange = { amountText = sanitizeAmountInput(it) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextPrimary,
                        ),
                    )
                }

                CurrencyPicker(code = fromCode, onSelected = onFromCodeChanged)
            }

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
                        .background(WaypointTerracotta)
                        .clickable { onSwapClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("⇄", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = toInfo.flag,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 8.dp),
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        // Currency modal label from strings.xml
                        text = stringResource(R.string.currency_modal_convert_to),
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
                            text = convertedResult?.let { formatCurrencyAmount(it) } ?: "–",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaypointTextPrimary,
                        )
                    }
                }

                CurrencyPicker(code = toCode, onSelected = onToCodeChanged)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    // Currency modal label from strings.xml
                    text = stringResource(R.string.currency_modal_market_rate),
                    fontSize = 13.sp,
                    color = WaypointTextMuted,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = rate?.let { "1 ${fromInfo.code} = ${formatCurrencyAmount(it)} ${toInfo.code}" } ?: "Loading rate...",
                    fontSize = 13.sp,
                    color = WaypointTerracotta,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun CurrencyPicker(
    code: String,
    onSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val info = currencyInfo(code)

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(WaypointCard)
                .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = info.code,
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
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(WaypointCard)
                .heightIn(max = 260.dp),
        ) {
            SUPPORTED_CURRENCIES.forEach { option ->
                val optionInfo = currencyInfo(option)
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(optionInfo.flag, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${optionInfo.code} - ${optionInfo.name}",
                                fontSize = 13.sp,
                                fontWeight = if (option == code) FontWeight.Bold else FontWeight.Normal,
                                color = if (option == code) WaypointTerracotta else WaypointTextPrimary,
                            )
                        }
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
