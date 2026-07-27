package com.example.prog7314

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Register screen. Frontend skeleton only, no auth logic wired up yet.
 */
class RegisterActivity : AppCompatActivity() {

    /**
     * Stub for the eventual Google sign-up flow. No network/auth calls yet,
     * just the hook the backend work will replace.
     */
    private fun onGoogleSignUpClicked() {
        // TODO: wire up Google sign-up once backend auth is ready.
    }

    /**
     * Stub for returning to the login screen. No navigation graph decided
     * yet, so this just closes the screen.
     */
    private fun onLogInClicked() {
        // TODO: navigate back to LoginActivity once nav wiring is decided.
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val root = findViewById<android.view.View>(R.id.register)
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

        findViewById<android.view.View>(R.id.btnGoogleSignUp).setOnClickListener {
            onGoogleSignUpClicked()
        }
        findViewById<android.view.View>(R.id.tvLogIn).setOnClickListener {
            onLogInClicked()
        }
    }
}
