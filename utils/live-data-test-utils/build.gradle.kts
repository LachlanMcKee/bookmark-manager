plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    dependencies = {
      implementation(
        Dependencies.Kotlin.stdlib,
        Dependencies.AndroidX.coreKtx,
        Dependencies.Compose.liveData,
        UnitTestDependencies.coroutinesTest
      )
    }
  )
}
