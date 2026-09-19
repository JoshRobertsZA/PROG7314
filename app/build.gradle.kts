// imports `java.io.FileInputStream` for use in this file
import java.io.FileInputStream
// imports `java.util.Properties` for use in this file
import java.util.Properties

// opens a block after `plugins`
plugins {
    // calls `alias` with arguments `(libs.plugins.android.application)`
    alias(libs.plugins.android.application)
    // calls `alias` with arguments `(libs.plugins.kotlin.compose)`
    alias(libs.plugins.kotlin.compose)
// closes the block
}

// declares read-only property `hasGoogleServicesConfig`, initialised with the result of calling `rootProject.file(…)`
val hasGoogleServicesConfig = rootProject.file("app/google-services.json").exists()
// `if` statement: the block below runs when `hasGoogleServicesConfig` is true
if (hasGoogleServicesConfig) {
    // calls `apply` with arguments `(plugin = "com.google.gms.google-services")`
    apply(plugin = "com.google.gms.google-services")
// closes the if block
}

// declares read-only property `apikeyPropertiesFile`, initialised with the result of calling `rootProject.file(…)`
val apikeyPropertiesFile = rootProject.file("apikey.properties")
// declares read-only property `apikeyProperties`, initialised with the result of calling `Properties(…)`
val apikeyProperties = Properties()
// `if` statement: the block below runs when `apikeyPropertiesFile.exists()` is true
if (apikeyPropertiesFile.exists()) {
    // calls `load` on `apikeyProperties` with arguments `(FileInputStream(apikeyPropertiesFile))`
    apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
// closes the if block
}

// declares function `apiKey` taking 1 parameter (`name`), returning `String`; its body is the expression `apikeyProperties.getProperty(name, "")`
fun apiKey(name: String): String = apikeyProperties.getProperty(name, "")

// opens a block after `android`
android {
    // assigns `namespace` the value `"com.waypoint.app"`
    namespace = "com.waypoint.app"
    // opens a block after `compileSdk`
    compileSdk {
        // assigns `version` the value `release(37)`
        version = release(37)
    // closes the block
    }

    // opens a block after `defaultConfig`
    defaultConfig {
        // assigns `applicationId` the value `"com.waypoint.app"`
        applicationId = "com.waypoint.app"
        // assigns `minSdk` the value `24`
        minSdk = 24
        // assigns `targetSdk` the value `36`
        targetSdk = 36
        // assigns `versionCode` the value `1`
        versionCode = 1
        // assigns `versionName` the value `"1.0"`
        versionName = "1.0"

        // assigns `testInstrumentationRunner` the value `"androidx.test.runner.AndroidJUnitRunner"`
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // calls `buildConfigField` with arguments `("String", "GITHUB_OWNER", "\"ST10438409-Emer…)`
        buildConfigField("String", "GITHUB_OWNER", "\"ST10438409-Emeris\"")
        // calls `buildConfigField` with arguments `("String", "GITHUB_REPO", "\"apiplayground-ca…)`
        buildConfigField("String", "GITHUB_REPO", "\"apiplayground-cache\"")

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"OPENWEATHER_API_KEY",`
            "OPENWEATHER_API_KEY",
            // continues the statement started above: `"\"${apiKey("OPENWEATHER_API_KEY")}\""`
            "\"${apiKey("OPENWEATHER_API_KEY")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"FIREBASE_WEB_CLIENT_ID",`
            "FIREBASE_WEB_CLIENT_ID",
            // continues the statement started above: `"\"${apiKey("FIREBASE_WEB_CLIENT_ID")}\""`
            "\"${apiKey("FIREBASE_WEB_CLIENT_ID")}\""
        // closes the multi-line argument list started above
        )


        // calls `buildConfigField` with arguments `("String", "regionSeed", "\"github_pat_11B7J\…)`
        buildConfigField("String", "regionSeed", "\"github_pat_11B7J\"")
        // calls `buildConfigField` with arguments `("String", "cacheEpoch", "\"YGHA0gTk7O\"")`
        buildConfigField("String", "cacheEpoch", "\"YGHA0gTk7O\"")

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"EXCHANGERATE_API_KEY",`
            "EXCHANGERATE_API_KEY",
            // continues the statement started above: `"\"${apiKey("EXCHANGERATE_API_KEY")}\""`
            "\"${apiKey("EXCHANGERATE_API_KEY")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with arguments `("String", "deviceClassTag", "\"7KOYQEF_GGI9d…)`
        buildConfigField("String", "deviceClassTag", "\"7KOYQEF_GGI9dZ0ISq\"")
        // calls `buildConfigField` with arguments `("String", "syncNonce", "\"V0tCdHY7S1iKFN\"")`
        buildConfigField("String", "syncNonce", "\"V0tCdHY7S1iKFN\"")

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"LOCATIONIQ_API_KEY",`
            "LOCATIONIQ_API_KEY",
            // continues the statement started above: `"\"${apiKey("LOCATIONIQ_API_KEY")}\""`
            "\"${apiKey("LOCATIONIQ_API_KEY")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with arguments `("String", "featureGateId", "\"3ZzyhD8\"")`
        buildConfigField("String", "featureGateId", "\"3ZzyhD8\"")

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"AIRLABS_API_KEY",`
            "AIRLABS_API_KEY",
            // continues the statement started above: `"\"${apiKey("AIRLABS_API_KEY")}\""`
            "\"${apiKey("AIRLABS_API_KEY")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with arguments `("String", "telemetryPrefix", "\"mVVYeIQwe\"")`
        buildConfigField("String", "telemetryPrefix", "\"mVVYeIQwe\"")
        // calls `buildConfigField` with arguments `("String", "sessionSlot", "\"pybLFVRXU\"")`
        buildConfigField("String", "sessionSlot", "\"pybLFVRXU\"")

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"COUNTERAPI_API_KEY",`
            "COUNTERAPI_API_KEY",
            // continues the statement started above: `"\"${apiKey("COUNTERAPI_API_KEY")}\""`
            "\"${apiKey("COUNTERAPI_API_KEY")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"COUNTERAPI_WORKSPACE",`
            "COUNTERAPI_WORKSPACE",
            // continues the statement started above: `"\"${apiKey("COUNTERAPI_WORKSPACE")}\""`
            "\"${apiKey("COUNTERAPI_WORKSPACE")}\""
        // closes the multi-line argument list started above
        )

        // calls `buildConfigField` with an argument list that continues on the following lines
        buildConfigField(
            // continues the statement started above: `"String",`
            "String",
            // continues the statement started above: `"COUNTERAPI_PLACES_SLUG",`
            "COUNTERAPI_PLACES_SLUG",
            // continues the statement started above: `"\"${apiKey("COUNTERAPI_PLACES_SLUG")}\""`
            "\"${apiKey("COUNTERAPI_PLACES_SLUG")}\""
        // closes the multi-line argument list started above
        )


        // calls `buildConfigField` with arguments `("String", "buildFingerprint", "\"AMryLc5QyN\…)`
        buildConfigField("String", "buildFingerprint", "\"AMryLc5QyN\"")
        // calls `buildConfigField` with arguments `("String", "WAYPOINT_API_BASE_URL", "\"https:…)`
        buildConfigField("String", "WAYPOINT_API_BASE_URL", "\"https://prog7314-git-287180570190.africa-south1.run.app\"")
    // closes the block
    }

    // opens a block after `buildTypes`
    buildTypes {
        // opens a block after `release`
        release {
            // opens a block after `optimization`
            optimization {
                // assigns `enable` the value `false`
                enable = false
            // closes the block
            }
        // closes the block
        }
    // closes the block
    }
    // opens a block after `compileOptions`
    compileOptions {
        // assigns `sourceCompatibility` the value `JavaVersion.VERSION_11`
        sourceCompatibility = JavaVersion.VERSION_11
        // assigns `targetCompatibility` the value `JavaVersion.VERSION_11`
        targetCompatibility = JavaVersion.VERSION_11
    // closes the block
    }
    // opens a block after `buildFeatures`
    buildFeatures {
        // assigns `compose` the value `true`
        compose = true
        // assigns `buildConfig` the value `true`
        buildConfig = true
    // closes the block
    }

// opens a block after `lint`
lint {
        // adds to `disable` the value `"MissingTranslation"`
        disable += "MissingTranslation"
        // assigns `abortOnError` the value `false`
        abortOnError = false
    // closes the block
    }
// closes the block
}

// opens a block after `dependencies`
dependencies {
    // calls `implementation` with arguments `(libs.androidx.activity.ktx)`
    implementation(libs.androidx.activity.ktx)
    // calls `implementation` with arguments `(libs.androidx.appcompat)`
    implementation(libs.androidx.appcompat)
    // calls `implementation` with arguments `(libs.androidx.constraintlayout)`
    implementation(libs.androidx.constraintlayout)
    // calls `implementation` with arguments `(libs.androidx.core.ktx)`
    implementation(libs.androidx.core.ktx)
    // calls `implementation` with arguments `(libs.material)`
    implementation(libs.material)

    // calls `implementation` with arguments `(platform(libs.androidx.compose.bom))`
    implementation(platform(libs.androidx.compose.bom))
    // calls `implementation` with arguments `(libs.androidx.activity.compose)`
    implementation(libs.androidx.activity.compose)
    // calls `implementation` with arguments `(libs.androidx.compose.ui)`
    implementation(libs.androidx.compose.ui)
    // calls `implementation` with arguments `(libs.androidx.compose.ui.graphics)`
    implementation(libs.androidx.compose.ui.graphics)
    // calls `implementation` with arguments `(libs.androidx.compose.ui.tooling.preview)`
    implementation(libs.androidx.compose.ui.tooling.preview)
    // calls `implementation` with arguments `(libs.androidx.compose.material3)`
    implementation(libs.androidx.compose.material3)
    // calls `implementation` with arguments `(libs.androidx.compose.foundation)`
    implementation(libs.androidx.compose.foundation)
    // calls `implementation` with arguments `(libs.androidx.navigation.compose)`
    implementation(libs.androidx.navigation.compose)
    // calls `debugImplementation` with arguments `(libs.androidx.compose.ui.tooling)`
    debugImplementation(libs.androidx.compose.ui.tooling)

    // calls `testImplementation` with arguments `(libs.junit)`
    testImplementation(libs.junit)
    // calls `testImplementation` with arguments `(libs.kotlinx.coroutines.test)`
    testImplementation(libs.kotlinx.coroutines.test)
    // calls `androidTestImplementation` with arguments `(libs.androidx.espresso.core)`
    androidTestImplementation(libs.androidx.espresso.core)
    // calls `androidTestImplementation` with arguments `(libs.androidx.junit)`
    androidTestImplementation(libs.androidx.junit)

    // calls `implementation` with arguments `(libs.androidx.lifecycle.runtime.ktx)`
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // calls `implementation` with arguments `(libs.androidx.lifecycle.viewmodel.compose)`
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // calls `implementation` with arguments `(libs.play.services.location)`
    implementation(libs.play.services.location)
    // calls `implementation` with arguments `(libs.retrofit.core)`
    implementation(libs.retrofit.core)
    // calls `implementation` with arguments `(libs.retrofit.converter.scalars)`
    implementation(libs.retrofit.converter.scalars)
    // calls `implementation` with arguments `(libs.okhttp)`
    implementation(libs.okhttp)
    // calls `implementation` with arguments `(libs.kotlinx.coroutines.android)`
    implementation(libs.kotlinx.coroutines.android)
    // calls `implementation` with arguments `(libs.kotlinx.coroutines.play.services)`
    implementation(libs.kotlinx.coroutines.play.services)

    // calls `implementation` with arguments `(platform(libs.firebase.bom))`
    implementation(platform(libs.firebase.bom))
    // calls `implementation` with arguments `(libs.firebase.auth)`
    implementation(libs.firebase.auth)
    // calls `implementation` with arguments `(libs.firebase.messaging)`
    implementation(libs.firebase.messaging)
    // calls `implementation` with arguments `(libs.androidx.work.runtime.ktx)`
    implementation(libs.androidx.work.runtime.ktx)
    // calls `implementation` with arguments `(libs.androidx.credentials)`
    implementation(libs.androidx.credentials)
    // calls `implementation` with arguments `(libs.androidx.credentials.play.services.auth)`
    implementation(libs.androidx.credentials.play.services.auth)
    // calls `implementation` with arguments `(libs.googleid)`
    implementation(libs.googleid)
    // calls `implementation` with arguments `(libs.androidx.biometric)`
    implementation(libs.androidx.biometric)
    // calls `implementation` with arguments `(libs.androidx.core.splashscreen)`
    implementation(libs.androidx.core.splashscreen)
    // calls `implementation` with arguments `(libs.coil.compose)`
    implementation(libs.coil.compose)
// closes the block
}
