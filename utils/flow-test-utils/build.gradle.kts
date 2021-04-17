plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    dependencies = {
      implementation(
        Dependencies.Kotlin.stdlib,
        Dependencies.AndroidX.coreKtx,
        UnitTestDependencies.coroutinesTest,
        UnitTestDependencies.junitApi,
        UnitTestDependencies.turbine
      )
    }
  )
}
