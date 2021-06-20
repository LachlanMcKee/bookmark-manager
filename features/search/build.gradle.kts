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
  implementation(libs.androidx.activityCompose)

  // DI
  implementation(libs.bundles.daggerRuntimes)
  kapt(libs.bundles.daggerCompilers)

  // Compose navigation factory
  implementation(libs.dagger.hilt.navigation.composeFactory.runtime)
  kapt(libs.dagger.hilt.navigation.composeFactory.compiler)

  implementation(libs.bundles.room)
  kapt(libs.room.compiler)

  implementation(libs.compose.paging)
  implementation(libs.timber)

  implementation(projects.features.common)
  implementation(projects.utils.composeNavigation)
  implementation(projects.components.list)
  implementation(projects.components.row)
  implementation(projects.components.chipLayouts)

  androidTestImplementation(libs.bundles.espressoCore)
  androidTestImplementation(libs.compose.test.junit4)
  debugImplementation(libs.compose.test.manifest)
}
