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
        targetSdk = 34
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
    //implementation("com.google.android.gms:play-services-wearable:18.1.0")

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:1.5.4")

    val navigationComposeVersion = "2.7.6"
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")
    implementation("androidx.navigation:navigation-runtime-ktx:$navigationComposeVersion")

    val wearComposeVersion = "1.2.1"
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeVersion")

    // for ongoing activity API
    implementation("androidx.wear:wear-ongoing:1.0.0")
    //implementation("androidx.core:core-ktx:2.2.0")
    implementation("androidx.core:core-ktx:1.12.0")



    //implementation fileTree(dir: 'libs', include: ['*.jar'])
    //implementation 'com.google.android.support:wearable:2.9.0'

    //implementation 'androidx.preference:preference-ktx:1.2.1'
    //implementation "androidx.core:core-splashscreen:1.0.1"
    //implementation 'androidx.recyclerview:recyclerview:1.3.2'
    //implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    //implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    //implementation 'androidx.wear:wear:1.3.0'
    //implementation 'androidx.core:core-ktx:1.12.0'
    //compileOnly 'com.google.android.wearable:wearable:2.9.0'
}
