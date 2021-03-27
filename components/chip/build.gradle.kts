plugins {
  id("net.lachlanmckee.bookmark.library")
}

apply {
  plugin("shot")
}

android {
  defaultConfig {
    testApplicationId = "net.lachlanmckee.bookmark"
  }
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.ComposeCore(project))

      implementation(Dependencies.Compose.iconsExtended)
    }
  )
}
