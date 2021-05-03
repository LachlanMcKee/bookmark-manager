plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.compose.navigation)
  implementation(libs.androidx.activityCompose) // TODO: Required as the navigation lib is using beta04 instead of beta05
  implementation(libs.dagger.hilt.navigation.compose)
}
