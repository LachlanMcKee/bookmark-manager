plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    dependencies = {
      implementation(Dependencies.Kotlin.stdlib)
    }
  )
}
