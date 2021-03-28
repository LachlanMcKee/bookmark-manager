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
      appendFrom(CommonDependencies.FeatureCore(project))

      implementation(
        project(":components:row"),
        project(":components:chip-layouts"),
        Dependencies.Compose.paging,
        Dependencies.Logging.timber
      )
    }
  )
}
