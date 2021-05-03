plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration()
}

dependencies {
  implementation(libs.timber)
  implementation(libs.espresso.runner)
  implementation(libs.dagger.hilt.androidTesting)
  implementation(libs.karumiShot)
}
