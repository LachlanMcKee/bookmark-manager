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
        Dependencies.Logging.timber
      )

      testImplementation(
        UnitTestDependencies.turbine
      )
    }
  )
}
