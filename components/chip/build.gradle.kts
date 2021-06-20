plugins {
  id("net.lachlanmckee.bookmark.library")
  id("shot")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)

  implementation(libs.compose.icons.extended)

  androidTestImplementation(libs.bundles.espressoCore)
  androidTestImplementation(libs.compose.test.junit4)
  debugImplementation(libs.compose.test.manifest)
}
