// opens a block after `plugins`
plugins {
    // calls `alias` with arguments `(libs.plugins.android.application)`
    alias(libs.plugins.android.application) apply false
    // calls `alias` with arguments `(libs.plugins.google.services)`
    alias(libs.plugins.google.services) apply false
// closes the block
}