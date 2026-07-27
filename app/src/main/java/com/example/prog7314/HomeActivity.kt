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
 */
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val root = findViewById<android.view.View>(R.id.home)
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
    }
}
