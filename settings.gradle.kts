pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "shot") {
        useModule("com.karumi:shot:5.13.0")
      }
    }
  }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "BookmarkManager"
include(
  ":app",
  ":utils:instrumentation-utils",
  ":utils:kotlin-utils",
  ":utils:flow-test-utils",
  ":components:chip-layouts",
  ":components:list",
  ":components:row",
  ":features:bookmark-form",
  ":features:common",
  ":features:home",
  ":features:search",
  ":features:settings"
)
includeBuild("plugins")
