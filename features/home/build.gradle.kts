plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.FeatureCore(project))

      implementation(
        project(":components:row"),
        project(":components:chip-layouts")
      )

      testImplementation(
        project(":utils:live-data-test-utils")
      )
    }
  )
}
