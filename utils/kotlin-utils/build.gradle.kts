plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration()
}

dependencies {
  implementation(libs.kotlin.stdlib.jdk7)
}
