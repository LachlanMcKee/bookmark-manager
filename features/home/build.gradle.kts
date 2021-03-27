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
      appendFrom(CommonDependencies.FeatureCore(project))

      implementation(
        project(":components:row"),
        project(":components:chip-layouts")
      )

      testImplementation(
        project(":utils:live-data-test-utils"),
        UnitTestDependencies.turbine
      )
    }
  )
}
