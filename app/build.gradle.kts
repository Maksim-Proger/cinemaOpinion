plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.pozmaxpav.cinemaopinion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pozmaxpav.cinemaopinion"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "verbeta2:3.3:07.11.2025"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/PMPav/keystore/mykeystore.jks")
            storePassword = "mysecretpassword"
            keyAlias = "mykeyalias"
            keyPassword = "mysecretpassword"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")  // Используем релизный ключ
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Modules
    implementation(project(":auth"))
    implementation(project(":intro"))
    implementation(project(":ui"))
    implementation(project(":core"))

    // WebView
    implementation(libs.webkit)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)

    // Navigation + Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Compose Navigation
    implementation(libs.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Okhttp3
    implementation(libs.logging.interceptor)

    // Coil
    implementation(libs.coil.compose)

    //Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Room
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // MaterialExpressive
    //implementation("androidx.compose.material3:material3-android:1.4.0-alpha14") // Разобраться почему Expressive работает только с этой версией

    implementation(libs.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
