plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.ComposeCore(project))

      implementation(
        project(":components:row"),
        Dependencies.Compose.liveData,
        Dependencies.Compose.paging
      )

      api(Dependencies.Compose.navigation)
    }
  )
}
