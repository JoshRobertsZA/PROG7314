package com.example.prog7314

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Temporary: navigate to the login screen until the real home/first
        // screen for the app is decided.
        findViewById<android.widget.Button>(R.id.btnGoToLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Temporary: jump straight to the trip calendar screen for testing,
        // bypassing login/home. Remove once there's a real nav path to it.
        findViewById<android.widget.Button>(R.id.btnGoToTripCalendar).setOnClickListener {
            startActivity(Intent(this, TripCalendarActivity::class.java))
        }

        // Temporary: jump straight to the view itinerary screen for
        // testing, bypassing login/home/calendar. Remove once there's a
        // real nav path into it.
        findViewById<android.widget.Button>(R.id.btnGoToViewItinerary).setOnClickListener {
            startActivity(Intent(this, ViewItineraryActivity::class.java))
        }

        // Temporary: navigate directly to the edit itinerary screen for
        // testing until it's wired into the app's real navigation.
        findViewById<android.widget.Button>(R.id.btnGoToEditItinerary).setOnClickListener {
            startActivity(Intent(this, EditItineraryActivity::class.java))
        }

        // Temporary: navigate directly to the all trips screen for testing
        // until it's wired into the app's real navigation.
        findViewById<android.widget.Button>(R.id.btnGoToAllTrips).setOnClickListener {
            startActivity(Intent(this, AllTripsActivity::class.java))
        }

        // Temporary: navigate directly to the nearby places screen for
        // testing until it's wired into the app's real navigation.
        findViewById<android.widget.Button>(R.id.btnGoToNearbyPlaces).setOnClickListener {
            startActivity(Intent(this, NearbyPlacesActivity::class.java))
        }
    }
}