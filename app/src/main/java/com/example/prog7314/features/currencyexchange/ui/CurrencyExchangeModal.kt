package com.example.prog7314.features.currencyexchange.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.SelectionModal
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary

/**
 * Currency Change modal (Figma node 392:12). Reached from the Profile
 * screen's Currency row (see SettingsScreen.kt), hosted in a Dialog by
 * the caller.
 *
 * TODO: replace the mock USD/ZAR rate with real ExchangeRate-API data,
 * make the two currency boxes pick from the app's supported currency
 * list, and persist the selection once onSaveClick fires - it's a no-op
 * for now.
 */
@Composable
fun CurrencyExchangeModal(onSaveClick: () -> Unit, modifier: Modifier = Modifier) {
    SelectionModal(
        title = stringResource(R.string.currency_modal_title),
        onSaveClick = onSaveClick,
        modifier = modifier,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CurrencyBox(code = "USD", rate = "1.00", modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.currency_modal_arrow_glyph),
                color = WaypointTerracotta,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            CurrencyBox(code = "ZAR", rate = "18.20", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CurrencyBox(code: String, rate: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(WaypointCream, RoundedCornerShape(RadiusButton))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(code, color = WaypointTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(rate, color = WaypointTextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
    }
}
