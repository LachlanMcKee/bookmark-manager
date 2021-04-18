plugins {
  id("net.lachlanmckee.bookmark.library")
  id("shot")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.FeatureCore(project))

      implementation(
        project(":components:list"),
        project(":components:row"),
        project(":components:chip-layouts"),
        Dependencies.Compose.paging,
        Dependencies.Logging.timber
      )

      implementation(Dependencies.Storage.roomRuntime)
      kapt(Dependencies.Storage.roomCompiler)
      implementation(Dependencies.Storage.roomKtx)
    }
  )
}
