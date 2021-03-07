plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration.composeModule {
    simpleFlowRow = true
  }
}

dependencies {
  implementation(project(":components:chip"))
}
