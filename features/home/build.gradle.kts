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
        project(":components:row"),
        project(":components:chip-layouts"),
        Dependencies.Logging.timber
      )

      testImplementation(
        project(":utils:live-data-test-utils"),
        UnitTestDependencies.turbine
      )
    }
  )
}
