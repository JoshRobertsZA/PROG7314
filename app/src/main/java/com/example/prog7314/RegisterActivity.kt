package com.example.prog7314

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Register screen. Frontend skeleton only, no auth logic wired up yet.
 *
 * Layout: [R.layout.activity_register] (Waypoint Figma node
 * 64:23, "02 Waypoint — Register"). Mirrors the structure of
 * LoginActivity so the two screens stay visually and behaviourally
 * consistent (same edge-to-edge handling, same click-stub pattern).
 */
class RegisterActivity : AppCompatActivity() {

    /**
     * Stub for the eventual Google sign-up flow. No network/auth calls yet,
     * just the hook the backend work will replace.
     *
     * When real auth is wired up, this is where the Google sign-up intent
     * (or credential manager call) should be launched.
     */
    private fun onGoogleSignUpClicked() {
        // TODO: wire up Google sign-up once backend auth is ready.
    }

    /**
     * Stub for returning to the login screen. No navigation graph decided
     * yet, so this just closes the screen and falls back to whatever
     * activity is underneath it on the back stack (LoginActivity, if the
     * user got here the normal way).
     */
    private fun onLogInClicked() {
        // TODO: navigate back to LoginActivity once nav wiring is decided.
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the layout draw behind the status/nav bars so the cream
        // background reaches the true edges of the screen.
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Edge-to-edge means the system bars can now overlap our content,
        // so we re-add their size as extra padding on top of whatever
        // padding the layout already defines (28dp sides, 64dp top, 36dp
        // bottom). Without this, the logo/footer would sit under the
        // status bar or nav bar on some devices.
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

        // Both taps just call their stub handler above. Keeping the
        // click-to-stub wiring here (rather than inline lambdas with logic)
        // means the eventual auth implementation only has to fill in the
        // two private functions, not touch onCreate again.
        findViewById<android.view.View>(R.id.btnGoogleSignUp).setOnClickListener {
            onGoogleSignUpClicked()
        }
        findViewById<android.view.View>(R.id.tvLogIn).setOnClickListener {
            onLogInClicked()
        }
    }
}
