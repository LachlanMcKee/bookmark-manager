plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)

  implementation(projects.components.row)

  implementation(libs.bundles.room)
  kapt(libs.room.compiler)

  api(libs.compose.navigation)
}
