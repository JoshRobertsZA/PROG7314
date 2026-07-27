package com.example.prog7314

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Trip calendar screen. Minimal skeleton whose only job is to display
 * activity_trip_calendar.xml and let the user navigate back - the day
 * grid, month nav, and itinerary buttons are still static/mock content
 * from the previous commit, not wired up yet.
 *
 * TODO: wire up month nav (btnPrevMonth/btnNextMonth), day selection,
 * tvEditTripName, and the itinerary buttons once those have somewhere to
 * go / data to act on.
 */
class TripCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_calendar)

        val root = findViewById<android.view.View>(R.id.tripCalendar)

        // Preserve the padding declared in XML and add system bar insets on
        // top of it, matching HomeActivity/LoginActivity.
        val basePaddingLeft = root.paddingLeft
        val basePaddingTop = root.paddingTop
        val basePaddingRight = root.paddingRight
        val basePaddingBottom = root.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                basePaddingLeft + systemBars.left,
                basePaddingTop + systemBars.top,
                basePaddingRight + systemBars.right,
                basePaddingBottom + systemBars.bottom
            )
            insets
        }

        // The only interaction this screen supports so far: leave the
        // screen the same way the system back gesture/button would.
        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
