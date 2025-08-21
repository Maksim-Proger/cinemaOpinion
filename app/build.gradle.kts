plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.pozmaxpav.cinemaopinion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pozmaxpav.cinemaopinion"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "ver:2.1:20.04.2025"

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

    // Module
    implementation(project(":introductoryscreens"))

    // LottieAnimation
    implementation(libs.lottie.compose)

    // WebView
    implementation(libs.webkit)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.57")
    kapt("com.google.dagger:hilt-compiler:2.57")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Okhttp3
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    //Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

    // Расширяем библиотеку с иконками
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

    // Room
    implementation("androidx.room:room-ktx:2.7.2")
    kapt("androidx.room:room-compiler:2.7.2")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

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