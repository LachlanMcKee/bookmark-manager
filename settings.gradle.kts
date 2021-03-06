pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "Bookmark Manager"
include(
  ":app",
  ":kotlin-utils",
  ":components:chip",
  ":components:chip-layouts",
  ":components:row"
)
includeBuild("plugins")
