plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = {
      implementation(Dependencies.Compose.navigation)
      implementation(Dependencies.Di.daggerHiltNavigationCompose)
    }
  )
}
