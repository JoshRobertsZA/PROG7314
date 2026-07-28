package com.example.prog7314

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Login screen. Frontend skeleton only, no auth logic wired up yet.
 *
 * TODO: btnGoogleSignIn currently navigates straight to HomeActivity as a
 * placeholder. Replace with a real Google Sign-In flow (and only navigate
 * to Home on success) once auth is implemented.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * Sends the user to the register screen. This is real navigation (not a
     * stub) since RegisterActivity exists and is reachable now; the auth
     * logic on either screen is still not wired up.
     */
    private fun onCreateAccountClicked() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val root = findViewById<android.view.View>(R.id.login)
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

        findViewById<android.view.View>(R.id.btnGoogleSignIn).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<android.view.View>(R.id.tvCreateAccount).setOnClickListener {
            onCreateAccountClicked()
        }
    }
}
