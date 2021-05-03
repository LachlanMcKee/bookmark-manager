plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.compose.navigation)
  implementation(libs.dagger.hilt.navigation.compose)
}
