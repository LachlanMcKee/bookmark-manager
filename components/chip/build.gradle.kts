plugins {
  id("net.lachlanmckee.bookmark.library")
  id("shot")
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
