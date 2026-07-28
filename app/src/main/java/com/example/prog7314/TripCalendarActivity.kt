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

        // Let the app draw behind the status/navigation bars instead of the
        // system automatically inserting padding for them; we handle that
        // padding ourselves below so it stacks with the XML-declared padding.
        enableEdgeToEdge()

        // Inflate activity_trip_calendar.xml as this screen's view hierarchy.
        setContentView(R.layout.activity_trip_calendar)

        // Root ConstraintLayout (@id/tripCalendar) that the XML-declared
        // padding (22dp sides, 28dp top, 24dp bottom) lives on.
        val root = findViewById<android.view.View>(R.id.tripCalendar)

        // Capture the padding declared in XML before it gets overwritten,
        // so the insets callback below can add to it instead of replacing
        // it outright.
        val basePaddingLeft = root.paddingLeft
        val basePaddingTop = root.paddingTop
        val basePaddingRight = root.paddingRight
        val basePaddingBottom = root.paddingBottom

        // Runs whenever the window insets change (e.g. on first layout, or
        // when the keyboard/system bars show or hide) and re-pads root so
        // its content never sits under the status bar, navigation bar, or
        // display cutouts, while still keeping the original XML padding -
        // matching the same pattern used in HomeActivity/LoginActivity.
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            // The status bar (top) and navigation bar (bottom/sides,
            // depending on orientation/gesture nav) insets for this frame.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Add the system bar insets on top of the original XML padding
            // on every side, rather than assigning the insets directly,
            // which would discard the 22dp/28dp/24dp spacing from the layout.
            v.setPadding(
                basePaddingLeft + systemBars.left,
                basePaddingTop + systemBars.top,
                basePaddingRight + systemBars.right,
                basePaddingBottom + systemBars.bottom
            )

            // Returning the insets unchanged (rather than CONSUMED) lets
            // any child views that also listen for insets still receive them.
            insets
        }

        // The only interaction this screen supports so far: leave the
        // screen the same way the system back gesture/button would.
        // Month nav, day selection, tvEditTripName, and the itinerary
        // buttons (see the TODO above) don't do anything yet - they're
        // still the static/mock content built in an earlier commit.
        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
