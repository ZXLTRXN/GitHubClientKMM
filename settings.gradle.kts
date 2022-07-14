enableFeaturePreview ("VERSION_CATALOGS")

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