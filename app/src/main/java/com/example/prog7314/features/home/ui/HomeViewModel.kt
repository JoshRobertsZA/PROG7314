package com.example.prog7314.features.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7314.core.location.LocationProvider
import com.example.prog7314.core.secrets.RemoteSecrets
import com.example.prog7314.features.home.data.CurrencyRepository
import com.example.prog7314.features.home.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 *
 * Extends AndroidViewModel so it can hand the Application context to
 * LocationProvider without holding an Activity reference.
 *
 * On creation it:
 *   1. Triggers RemoteSecrets to load GITHUB_WRITE_TOKEN and other keys.
 *   2. Fetches weather for the default city (cache-first).
 *   3. Fetches the USD/ZAR rate (cache-first).
 *   4. Starts collecting the device's live location.
 *
 * The UI drives refreshes via selectCity(), selectFromCurrency(),
 * refreshWeather(), and refreshCurrency().
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Load remote keys first so the write token is ready for cache writes
            RemoteSecrets.ensureLoaded()
            val s = _uiState.value
            loadWeather(s.selectedCity)
            loadCurrency(s.selectedFromCurrency)
        }
        startLocationUpdates()
    }

    // ── Public surface ─────────────────────────────────────────────────────────

    /** Called when the user picks a new city from the city-search dialog. */
    fun selectCity(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
        loadWeather(city)
    }

    /** Called when the user changes the source currency in the currency modal. */
    fun selectFromCurrency(code: String) {
        _uiState.update { it.copy(selectedFromCurrency = code) }
        loadCurrency(code)
    }

    fun refreshWeather() = loadWeather(_uiState.value.selectedCity)
    fun refreshCurrency() = loadCurrency(_uiState.value.selectedFromCurrency)

    // ── Private helpers ────────────────────────────────────────────────────────

    private fun loadWeather(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(weather = WeatherState.Loading) }
            val result = WeatherRepository.getWeather(city)
            _uiState.update {
                it.copy(
                    weather = if (result != null) WeatherState.Success(result)
                              else WeatherState.Error,
                )
            }
        }
    }

    private fun loadCurrency(fromCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(currency = CurrencyState.Loading) }
            val result = CurrencyRepository.getRate(fromCode)
            _uiState.update {
                it.copy(
                    currency = if (result != null) CurrencyState.Success(result)
                               else CurrencyState.Error,
                )
            }
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            LocationProvider.locationFlow(getApplication())
                .catch { /* permission not granted or provider error — stay silent */ }
                .collect { loc -> _uiState.update { it.copy(location = loc) } }
        }
    }
}
