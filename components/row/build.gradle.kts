plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(composeEnabled = true)
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)
  implementation(projects.utils.kotlinUtils)
}
