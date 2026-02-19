pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    
    plugins {
        kotlin("jvm") version "2.0.21"
        kotlin("plugin.serialization") version "2.0.21"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://jitpack.io")
    }
}

rootProject.name = "gsi-core"
