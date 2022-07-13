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

val coroutineVersion = "1.6.0"
val hiltVersion = "2.42"
val lifecycleVersion = "2.4.1"
val fragmentVersion = "1.4.1"
val navigationVersion = "2.4.1"
val splashVersion = "1.0.0-beta02"
val markwonVersion = "4.6.2"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.core:core-splashscreen:${splashVersion}")
    implementation("androidx.fragment:fragment-ktx:${fragmentVersion}")

//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Dagger
    implementation("com.google.dagger:hilt-android:${hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${hiltVersion}")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navigationVersion}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${navigationVersion}")

    // Markdown
    implementation("io.noties.markwon:core:${markwonVersion}")
}

kapt {
    correctErrorTypes = true
}