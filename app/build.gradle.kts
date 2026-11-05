import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val hasGoogleServicesConfig = rootProject.file("app/google-services.json").exists()
if (hasGoogleServicesConfig) {
    apply(plugin = "com.google.gms.google-services")
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
if (apikeyPropertiesFile.exists()) {
    apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

fun apiKey(name: String): String = apikeyProperties.getProperty(name, "")

android {
    namespace = "com.waypoint.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.waypoint.app"
        // Android 8.0+: java.time is used throughout for trip dates
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            "\"${apiKey("OPENWEATHER_API_KEY")}\""
        )

        buildConfigField(
            "String",
            "FIREBASE_WEB_CLIENT_ID",
            "\"${apiKey("FIREBASE_WEB_CLIENT_ID")}\""
        )


        buildConfigField(
            "String",
            "EXCHANGERATE_API_KEY",
            "\"${apiKey("EXCHANGERATE_API_KEY")}\""
        )

        buildConfigField(
            "String",
            "LOCATIONIQ_API_KEY",
            "\"${apiKey("LOCATIONIQ_API_KEY")}\""
        )

        buildConfigField(
            "String",
            "AIRLABS_API_KEY",
            "\"${apiKey("AIRLABS_API_KEY")}\""
        )

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

        buildConfigField(
            "String",
            "WAYPOINT_API_BASE_URL",
            "\"https://prog7314-git-287180570190.africa-south1.run.app\""
        )
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }
    buildTypes {
        release {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            optimization {
                enable = true
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

// MissingTranslation is no longer disabled, so lint flags any English string with no zu or xh version
    lint {
        abortOnError = false
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
        // Android's own org.json classes are stub jars on the unit-test
        // classpath (every method throws "not mocked") - a real implementation
        // here shadows those stubs so OutboxCoalescingTest's JSONObject calls
        // actually work. Test-only; never ships in the app.
        testImplementation(libs.json)
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
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.coil.compose)
        implementation(libs.androidx.datastore.preferences)
    }
}