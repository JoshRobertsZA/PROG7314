package com.example.prog7314

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Home screen. Frontend skeleton only: static/mock content matching the
 * Waypoint Figma design (node 47:30), no networking or navigation wired up
 * yet. This is a separate screen from MainActivity/activity_main; the two
 * are not linked together at this stage.
 *
 * TODO: replace mock trip/weather/currency/nearby-places content with real
 * data once the backend (LocationIQ, OpenWeatherMap, ExchangeRate-API) is
 * wired up on its own branch.
 * TODO: wire up bottomNav (navTrips/navMap/navProfile), btnSettings, the
 * trip action buttons, and the map CTA banner once those destinations exist.
 */
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the app draw behind the status/navigation bars instead of the
        // system automatically inserting padding for them; we handle that
        // padding ourselves below so it stacks with the XML-declared padding.
        enableEdgeToEdge()

        // Inflate activity_home.xml as this screen's view hierarchy.
        setContentView(R.layout.activity_home)

        // Root ConstraintLayout (@id/home) that the XML-declared padding
        // (22dp sides, 28dp top) lives on.
        val root = findViewById<android.view.View>(R.id.home)

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
        // matching the same pattern used in LoginActivity.
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            // The status bar (top) and navigation bar (bottom/sides,
            // depending on orientation/gesture nav) insets for this frame.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Add the system bar insets on top of the original XML padding
            // on every side, rather than assigning the insets directly,
            // which would discard the 22dp/28dp spacing from the layout.
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
    }
}
