plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)

  implementation(libs.compose.icons.extended)
  implementation(libs.compose.flowLayout)
  implementation(libs.compose.placeholder)

  androidTestImplementation(libs.bundles.espressoCore)
}
