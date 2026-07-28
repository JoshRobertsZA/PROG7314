package com.example.prog7314

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Edit itinerary screen. Minimal skeleton whose only job is to display
 * activity_edit_itinerary.xml and let the user navigate back - the weather
 * chips, every booking/place row, and the upload/add/undo/edit/remove
 * controls are still static/mock content from the previous commit, not
 * wired up yet.
 *
 * TODO: wire up the "+ Upload"/"+ Add" chips, the per-row ↺ (replace),
 * ✎ (edit), and ✕ (remove) icon buttons, and the "+ Add entertainment"
 * placeholder, and replace mock weather/flight/lodging/car/restaurant/
 * attraction content with real data, once the backend (booking storage,
 * OpenWeatherMap) is wired up on its own branch.
 */
class EditItineraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the app draw behind the status/navigation bars instead of the
        // system automatically inserting padding for them; we handle that
        // padding ourselves below so it stacks with the XML-declared padding.
        enableEdgeToEdge()

        // Inflate activity_edit_itinerary.xml as this screen's view hierarchy.
        setContentView(R.layout.activity_edit_itinerary)

        // Root ConstraintLayout (@id/editItinerary) that the XML-declared
        // padding (22dp sides, 28dp top, 32dp bottom) lives on.
        val root = findViewById<android.view.View>(R.id.editItinerary)

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
        // matching the same pattern used in HomeActivity/TripCalendarActivity/
        // ViewItineraryActivity.
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            // The status bar (top) and navigation bar (bottom/sides,
            // depending on orientation/gesture nav) insets for this frame.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Add the system bar insets on top of the original XML padding
            // on every side, rather than assigning the insets directly,
            // which would discard the 22dp/28dp/32dp spacing from the layout.
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
        // screen the same way the system back gesture/button would. Every
        // other control (see the TODO above) is still the static/mock
        // content built in the previous commit.
        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
