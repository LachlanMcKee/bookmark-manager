plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(projects.components.chip)

  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)

  implementation(libs.compose.icons.extended)
  implementation(libs.compose.flowLayout)

  androidTestImplementation(libs.bundles.espressoCore)
}
