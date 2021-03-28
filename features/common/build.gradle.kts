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
        Dependencies.Compose.liveData
      )

      implementation(Dependencies.Storage.roomRuntime)
      kapt(Dependencies.Storage.roomCompiler)
      implementation(Dependencies.Storage.roomKtx)

      api(Dependencies.Compose.navigation)
    }
  )
}
