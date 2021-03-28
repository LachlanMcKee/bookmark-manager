plugins {
  id("net.lachlanmckee.bookmark.library")
}

apply {
  plugin("shot")
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
