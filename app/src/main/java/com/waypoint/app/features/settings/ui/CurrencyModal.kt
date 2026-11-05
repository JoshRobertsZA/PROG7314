package com.waypoint.app.features.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waypoint.app.R
import com.waypoint.app.core.common.SelectionModal
import com.waypoint.app.features.currencyexchange.ui.SUPPORTED_CURRENCIES
import com.waypoint.app.features.currencyexchange.ui.currencyInfo

/** Picks the home currency: the one the Home converter converts into by default. */
@Composable
fun CurrencyModal(
    selectedCode: String,
    onCurrencySelected: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectionModal(
        title = stringResource(R.string.settings_home_currency_modal_title),
        onSaveClick = onSaveClick,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(max = 360.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(SUPPORTED_CURRENCIES, key = { it }) { code ->
                val info = currencyInfo(code)
                SelectionOptionRow(
                    flag = info.flag,
                    title = info.code,
                    subtitle = info.name,
                    selected = code == selectedCode,
                    onClick = { onCurrencySelected(code) },
                )
            }
        }
    }
}
