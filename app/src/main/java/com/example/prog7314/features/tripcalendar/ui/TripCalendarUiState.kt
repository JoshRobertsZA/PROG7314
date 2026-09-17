package com.example.prog7314.features.tripcalendar.ui

import java.time.LocalDate
import java.time.YearMonth

data class TripCalendarUiState(
    val isLoading: Boolean = true,
    val tripId: String = "",
    val tripName: String = "",
    val destination: String? = null,
    val destLat: Double? = null,
    val destLng: Double? = null,
    val nightCount: Int = 0,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val displayMonth: YearMonth = YearMonth.now(),
    val dateRangeLabel: String = "",
    val dayCountLabel: String = "",
    // Edit name dialog
    val showNameDialog: Boolean = false,
    val nameInput: String = "",
    // Destination search
    val showDestSearch: Boolean = false,
    val isGeocodingDest: Boolean = false,
)
