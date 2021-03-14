plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    dependencies = {
      implementation(Dependencies.Logging.timber)
      implementation(EspressoTestDependencies.runner)
      implementation(EspressoTestDependencies.daggerHiltAndroidTesting)
    }
  )
}
