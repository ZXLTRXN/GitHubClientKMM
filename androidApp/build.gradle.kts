plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.example.githubclientkmm.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(libs.splash)
    implementation(libs.fragment)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    implementation(libs.bundles.navigation)
    implementation(libs.markwon)
}

kapt {
    correctErrorTypes = true
}