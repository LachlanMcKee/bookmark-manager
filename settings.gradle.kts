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
  ":utils:instrumentation-utils",
  ":utils:kotlin-utils",
  ":utils:live-data-test-utils",
  ":components:chip",
  ":components:chip-layouts",
  ":components:row",
  ":features:common",
  ":features:home",
  ":features:search",
  ":features:settings"
)
includeBuild("plugins")
