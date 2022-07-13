buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val navigationVersion = "2.4.1"
        val hiltVersion = "2.41"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${hiltVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${navigationVersion}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}