plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.ComposeCore(project))

      implementation(
        project(":components:chip-layouts"),
        project(":components:row"),
        project(":features:common"),

        Dependencies.AndroidX.activityCompose,
        Dependencies.AndroidX.lifecycleViewModelKtx,
        Dependencies.AndroidX.lifecycleLiveDataKtx,
        Dependencies.Compose.liveData,
        Dependencies.Di.dagger,
        Dependencies.Di.daggerHilt
      )
      kapt(
        Dependencies.Di.daggerCompiler,
        Dependencies.Di.daggerHiltCompiler
      )
    }
  )
}
