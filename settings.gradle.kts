pluginManagement {
  repositories {
    gradlePluginPortal()
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
