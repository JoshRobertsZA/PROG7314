import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// google-services.json isn't committed until the team's Firebase project is
// set up (it's per-project config, downloaded from the Firebase console).
// Only apply the Google Services plugin once that file actually exists, so
// a fresh clone without it still builds - see README for setup steps.
val hasGoogleServicesConfig = rootProject.file("app/google-services.json").exists()
if (hasGoogleServicesConfig) {
    apply(plugin = "com.google.gms.google-services")
}

// Load local API keys from apikey.properties (gitignored, never committed).
// Copy apikey.properties.example -> apikey.properties and fill in real values.
val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
if (apikeyPropertiesFile.exists()) {
    apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
}

fun apiKey(name: String): String = apikeyProperties.getProperty(name, "")

android {
    namespace = "com.waypoint.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.waypoint.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GITHUB_OWNER", "\"ST10438409-Emeris\"")
        buildConfigField("String", "GITHUB_REPO", "\"apiplayground-cache\"")

        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            "\"${apiKey("OPENWEATHER_API_KEY")}\""
        )

        // OAuth "Web client ID" for Firebase Google Sign-In (Authentication ->
        // Sign-in method -> Google -> Web SDK configuration in the Firebase
        // console). See GoogleSignInHelper.kt for why this is a BuildConfig
        // field instead of the usual google-services.json-generated resource.
        buildConfigField(
            "String",
            "FIREBASE_WEB_CLIENT_ID",
            "\"${apiKey("FIREBASE_WEB_CLIENT_ID")}\""
        )


        // Bootstrap token pieces (read-only, apiplayground-cache repo only).
        // Reassembled at runtime in RemoteSecrets.kt. Do not reorder these
        // without also updating the join order there.
        buildConfigField("String", "regionSeed", "\"github_pat_11B7J\"")
        buildConfigField("String", "cacheEpoch", "\"YGHA0gTk7O\"")

        buildConfigField(
            "String",
            "EXCHANGERATE_API_KEY",
            "\"${apiKey("EXCHANGERATE_API_KEY")}\""
        )

        buildConfigField("String", "deviceClassTag", "\"7KOYQEF_GGI9dZ0ISq\"")
        buildConfigField("String", "syncNonce", "\"V0tCdHY7S1iKFN\"")

        buildConfigField(
            "String",
            "LOCATIONIQ_API_KEY",
            "\"${apiKey("LOCATIONIQ_API_KEY")}\""
        )

        buildConfigField("String", "featureGateId", "\"3ZzyhD8\"")

        buildConfigField(
            "String",
            "AIRLABS_API_KEY",
            "\"${apiKey("AIRLABS_API_KEY")}\""
        )

        buildConfigField("String", "telemetryPrefix", "\"mVVYeIQwe\"")
        buildConfigField("String", "sessionSlot", "\"pybLFVRXU\"")

        buildConfigField(
            "String",
            "COUNTERAPI_API_KEY",
            "\"${apiKey("COUNTERAPI_API_KEY")}\""
        )

        buildConfigField(
            "String",
            "COUNTERAPI_WORKSPACE",
            "\"${apiKey("COUNTERAPI_WORKSPACE")}\""
        )

        buildConfigField(
            "String",
            "COUNTERAPI_PLACES_SLUG",
            "\"${apiKey("COUNTERAPI_PLACES_SLUG")}\""
        )


        buildConfigField("String", "buildFingerprint", "\"AMryLc5QyN\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

lint {
        disable += "MissingTranslation"
        abortOnError = false
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.play.services.location)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.biometric)
    implementation(libs.coil.compose)
}
