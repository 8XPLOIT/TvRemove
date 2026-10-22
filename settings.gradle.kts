pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
dependencyResolutionManagement { repositories { google(); mavenCentral(); maven("https://jitpack.io") } }
rootProject.name = "TvRemote"
include(":app")
