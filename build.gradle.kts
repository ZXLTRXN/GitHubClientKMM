buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlinPlugin)
        classpath(libs.gradlePlugin)
        classpath(libs.hiltPlugin)
        classpath(libs.navigatinSafeArgsPlugin)
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