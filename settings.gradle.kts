enableFeaturePreview ("VERSION_CATALOGS")
enableFeaturePreview ("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "GitHubClientKMM"
include(":androidApp")
include(":shared")