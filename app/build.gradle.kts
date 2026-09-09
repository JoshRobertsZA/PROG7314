import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
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
    namespace = "com.example.prog7314"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.prog7314"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            "\"${apiKey("OPENWEATHER_API_KEY")}\""
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
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
