pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "Bookmark Manager"
include(
  ":app",
  ":utils:applitools-compose",
  ":utils:kotlin-utils",
  ":components:chip",
  ":components:chip-layouts",
  ":components:row"
)
includeBuild("plugins")
