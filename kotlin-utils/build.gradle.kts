import net.lachlanmckee.bookmark.ModuleConfiguration

plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    testingMode = ModuleConfiguration.TestingMode.UNIT,
    dependencies = ModuleConfiguration.Dependencies(
      kotlin = ModuleConfiguration.Kotlin(utils = false)
    )
  )
}
