pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "shot") {
        useModule("com.karumi:shot:5.10.3")
      }
    }
  }
}

rootProject.name = "Bookmark Manager"
include(
  ":app",
  ":utils:compose-navigation",
  ":utils:instrumentation-utils",
  ":utils:kotlin-utils",
  ":utils:flow-test-utils",
  ":components:chip",
  ":components:chip-layouts",
  ":components:list",
  ":components:row",
  ":features:common",
  ":features:home",
  ":features:search",
  ":features:settings"
)
includeBuild("plugins")
