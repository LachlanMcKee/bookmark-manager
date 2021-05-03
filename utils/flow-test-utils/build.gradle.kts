plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration()
}

dependencies {
  implementation(libs.kotlin.stdlib.jdk7)
  implementation(libs.androidx.coreKtx)
  implementation(libs.kotlin.coroutines.test)
  implementation(libs.junit.api)
  implementation(libs.turbine)
}
