plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "net.osdn.gokigen.joggingtimer"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.osdn.gokigen.joggingtimer"
        minSdk = 26
        targetSdk = 33
        versionCode = 2000000
        versionName = "2.0.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.activity:activity-compose:1.8.2")

    //implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    //implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    //implementation("androidx.compose.material:material:1.6.0")

    //val navigationComposeVersion = "2.6.0"
    val navigationComposeVersion = "2.7.6"
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")
    implementation("androidx.navigation:navigation-runtime-ktx:$navigationComposeVersion")

    val wearComposeVersion = "1.2.1"
    //val wearComposeVersion = "1.3.0"    // cannot handle button long press
    //val wearComposeVersion = "1.4.0-alpha01"
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-ui-tooling:$wearComposeVersion")

    // Horologist
    implementation("com.google.android.horologist:horologist-composables:0.4.17")

    // for ongoing activity API
    implementation("androidx.wear:wear-ongoing:1.0.0")
    implementation("androidx.core:core-ktx:1.12.0")

}
