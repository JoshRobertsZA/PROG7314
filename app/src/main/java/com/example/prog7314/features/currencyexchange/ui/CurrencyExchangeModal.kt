package com.example.prog7314.features.currencyexchange.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.prog7314.R
import com.example.prog7314.core.common.SelectionModal
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary

// Common currencies the user can pick as the FROM currency.
private val SUPPORTED_CURRENCIES = listOf(
    "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY",
    "INR", "BRL", "MXN", "SGD", "HKD", "NOK", "SEK", "DKK",
    "NZD", "KES", "NGN", "EGP",
)

/**
 * Currency exchange modal (Figma node 392:12).
 *
 * The modal always shows the conversion TO ZAR. Optional parameters wire
 * in real ExchangeRate-API data from [HomeViewModel]; all parameters have
 * sensible defaults so the existing call in SettingsScreen compiles as-is:
 *
 *   CurrencyExchangeModal(onSaveClick = { ... })
 *
 * @param fromCode      Currently selected FROM currency (default "USD").
 * @param rate          Live conversion rate to ZAR; null = show placeholder.
 * @param isLoading     True while the rate is being fetched.
 * @param onFromCodeChanged  Called when the user picks a different currency.
 */
@Composable
fun CurrencyExchangeModal(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    fromCode: String = "USD",
    rate: Double? = null,
    isLoading: Boolean = false,
    onFromCodeChanged: (String) -> Unit = {},
) {
    var showCurrencyPicker by remember { mutableStateOf(false) }

    if (showCurrencyPicker) {
        Dialog(onDismissRequest = { showCurrencyPicker = false }) {
            Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 4.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select currency",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WaypointTextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    LazyColumn {
                        items(SUPPORTED_CURRENCIES) { code ->
                            Text(
                                text = code,
                                color = if (code == fromCode) WaypointTerracotta else WaypointTextPrimary,
                                fontWeight = if (code == fromCode) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onFromCodeChanged(code)
                                        showCurrencyPicker = false
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

    SelectionModal(
        title = stringResource(R.string.currency_modal_title),
        onSaveClick = onSaveClick,
        modifier = modifier,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // FROM box — tappable to change currency
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showCurrencyPicker = true },
            ) {
                CurrencyBox(
                    code = fromCode,
                    rate = "1.00",
                    modifier = Modifier.fillMaxWidth(),
                    isSelected = true,
                )
            }
            Text(
                text = stringResource(R.string.currency_modal_arrow_glyph),
                color = WaypointTerracotta,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            // TO box — always ZAR, shows live rate
            Box(modifier = Modifier.weight(1f)) {
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WaypointCream, RoundedCornerShape(RadiusButton))
                            .border(1.5.dp, WaypointBorderSoft, RoundedCornerShape(RadiusButton))
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("ZAR", color = WaypointTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 4.dp),
                            color = WaypointTerracotta,
                            strokeWidth = 2.dp,
                        )
                    }
                } else {
                    CurrencyBox(
                        code = "ZAR",
                        rate = rate?.let { "%.2f".format(it) } ?: "–",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyBox(
    code: String,
    rate: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(WaypointCream, RoundedCornerShape(RadiusButton))
            .border(
                width = if (isSelected) 2.dp else 1.5.dp,
                color = if (isSelected) WaypointTerracotta else WaypointBorderSoft,
                shape = RoundedCornerShape(RadiusButton),
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(code, color = WaypointTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(rate, color = WaypointTextMuted, fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
    }
}
