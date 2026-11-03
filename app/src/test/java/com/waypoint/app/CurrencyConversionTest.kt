// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.cache.CurrencyRatesCache` for use in this file
import com.waypoint.app.core.cache.CurrencyRatesCache
// imports `com.waypoint.app.features.currencyexchange.ui.convertAmount` for use in this file
import com.waypoint.app.features.currencyexchange.ui.convertAmount
// imports `com.waypoint.app.features.currencyexchange.ui.formatCurrencyAmount` for use in this file
import com.waypoint.app.features.currencyexchange.ui.formatCurrencyAmount
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Assert.assertFalse` for use in this file
import org.junit.Assert.assertFalse
// imports `org.junit.Assert.assertNull` for use in this file
import org.junit.Assert.assertNull
// imports `org.junit.Assert.assertTrue` for use in this file
import org.junit.Assert.assertTrue
// imports `org.junit.Test` for use in this file
import org.junit.Test
// imports `java.util.Locale` for use in this file
import java.util.Locale

// declares class `CurrencyConversionTest` and opens its body
class CurrencyConversionTest {

    // declares private read-only property `usdRates`, a small USD based rates table shared by the tests below
    private val usdRates = CurrencyRatesCache(
        // continues the statement started above: `baseCode = "USD",`
        baseCode = "USD",
        // continues the statement started above: `rates = mapOf("ZAR" to 18.5, "EUR" to 0.92, "JPY" to 150.0, "USD" to 1.0),`
        rates = mapOf("ZAR" to 18.5, "EUR" to 0.92, "JPY" to 150.0, "USD" to 1.0),
        // continues the statement started above: `fetchedAtMs = System.currentTimeMillis(),`
        fetchedAtMs = System.currentTimeMillis(),
    // closes the multi-line argument list started above
    )

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `convertAmount_multipliesAmountByRate`
    fun convertAmount_multipliesAmountByRate() {
        // checks that 10 USD at a rate of 18.5 gives 185 ZAR
        assertEquals(185.0, convertAmount(10.0, 18.5), 0.0001)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `convertAmount_zeroAmount_returnsZero`
    fun convertAmount_zeroAmount_returnsZero() {
        // checks that converting nothing gives nothing, whatever the rate is
        assertEquals(0.0, convertAmount(0.0, 18.5), 0.0)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `convertAmount_smallRate_keepsPrecision`
    fun convertAmount_smallRate_keepsPrecision() {
        // checks that 1000 JPY at a rate of 0.0067 gives 6.7 USD without losing the small rate
        assertEquals(6.7, convertAmount(1000.0, 0.0067), 0.0001)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `convertAmount_roundTrip_returnsOriginalAmount`
    fun convertAmount_roundTrip_returnsOriginalAmount() {
        // declares read-only property `usdToEur`, initialised with the USD to EUR rate from the table
        val usdToEur = usdRates.rateFor("EUR")!!
        // declares read-only property `there`, the amount after converting 100 USD to EUR
        val there = convertAmount(100.0, usdToEur)
        // declares read-only property `back`, the amount after converting that EUR amount back to USD with the inverse rate
        val back = convertAmount(there, 1.0 / usdToEur)
        // checks that converting there and back returns the original 100 USD
        assertEquals(100.0, back, 0.000001)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `formatCurrencyAmount_valueOverOne_usesTwoDecimals`
    fun formatCurrencyAmount_valueOverOne_usesTwoDecimals() {
        // checks that a value over one rounds to 2 decimals
        assertEquals("18.46", formatCurrencyAmount(18.456))
        // checks that rounding goes up when the third decimal is 5 or more
        assertEquals("1.24", formatCurrencyAmount(1.236))
        // checks that a large amount keeps 2 decimals and no thousands separator
        assertEquals("1234.50", formatCurrencyAmount(1234.5))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `formatCurrencyAmount_zero_usesTwoDecimals`
    fun formatCurrencyAmount_zero_usesTwoDecimals() {
        // checks that zero shows as 0.00
        assertEquals("0.00", formatCurrencyAmount(0.0))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `formatCurrencyAmount_valueBelowOne_usesFourDecimals`
    fun formatCurrencyAmount_valueBelowOne_usesFourDecimals() {
        // checks that a value between 0.01 and 1 shows 4 decimals
        assertEquals("0.5432", formatCurrencyAmount(0.5432))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `formatCurrencyAmount_tinyValue_usesSixDecimals`
    fun formatCurrencyAmount_tinyValue_usesSixDecimals() {
        // checks that a very small rate such as JPY to USD does not collapse to 0.00
        assertEquals("0.006700", formatCurrencyAmount(0.0067))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `formatCurrencyAmount_ignoresDeviceLocale`
    fun formatCurrencyAmount_ignoresDeviceLocale() {
        // declares read-only property `original`, the default locale before the test changes it
        val original = Locale.getDefault()
        // `try` block: the locale is restored in the `finally` below even if the assertion fails
        try {
            // sets the default locale to German, which normally uses a comma as the decimal separator
            Locale.setDefault(Locale.GERMANY)
            // checks that the output still uses a dot, so it matches the amount input field
            assertEquals("1234.50", formatCurrencyAmount(1234.5))
        // `finally` block: always runs after the try block
        } finally {
            // restores the original default locale
            Locale.setDefault(original)
        // closes the finally block
        }
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `convertAndFormat_acrossPairs_givesExpectedDisplay`
    fun convertAndFormat_acrossPairs_givesExpectedDisplay() {
        // checks 10 USD to ZAR through the rates table and the display format
        assertEquals("185.00", formatCurrencyAmount(convertAmount(10.0, usdRates.rateFor("ZAR")!!)))
        // checks 10 USD to EUR, which has a floating point result of 9.200000000000001 before formatting
        assertEquals("9.20", formatCurrencyAmount(convertAmount(10.0, usdRates.rateFor("EUR")!!)))
        // checks 10 USD to JPY, a large target amount
        assertEquals("1500.00", formatCurrencyAmount(convertAmount(10.0, usdRates.rateFor("JPY")!!)))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `rateFor_baseCode_returnsOne`
    fun rateFor_baseCode_returnsOne() {
        // checks that the base currency always converts to itself at 1.0, even when missing from the table
        val noSelf = CurrencyRatesCache("EUR", mapOf("USD" to 1.08), System.currentTimeMillis())
        // checks the value of `noSelf.rateFor("EUR")`
        assertEquals(1.0, noSelf.rateFor("EUR")!!, 0.0)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `rateFor_knownCode_returnsRate`
    fun rateFor_knownCode_returnsRate() {
        // checks that a currency in the table returns its rate
        assertEquals(18.5, usdRates.rateFor("ZAR")!!, 0.0)
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `rateFor_unknownCode_returnsNull`
    fun rateFor_unknownCode_returnsNull() {
        // checks that a currency missing from the table returns null instead of a wrong rate
        assertNull(usdRates.rateFor("XYZ"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `ratesCache_fetchedToday_isNotStale`
    fun ratesCache_fetchedToday_isNotStale() {
        // checks that a table fetched just now is not stale
        assertFalse(usdRates.isStale())
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `ratesCache_fetchedTwoDaysAgo_isStale`
    fun ratesCache_fetchedTwoDaysAgo_isStale() {
        // declares read-only property `old`, a rates table fetched 48 hours ago
        val old = usdRates.copy(fetchedAtMs = System.currentTimeMillis() - 48L * 60 * 60 * 1000)
        // checks that a table from two days ago is stale and will be refreshed
        assertTrue(old.isStale())
    // closes the function
    }
// closes the class `CurrencyConversionTest`
}
