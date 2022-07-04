plugins {
  id("net.lachlanmckee.bookmark.app")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
  id("shot")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    useHiltWithinAndroidTest = true
  )
}

android {
  defaultConfig {
    applicationId = "net.lachlanmckee.bookmark"
    versionCode = System.getenv("BITRISE_BUILD_NUMBER")?.toIntOrNull() ?: 1
    versionName = "0.0.1"
  }

  signingConfigs {
    getByName("debug") {
      storeFile = file("src/debug/debug-keystore.jks")
      storePassword = "key-store-password"
      keyAlias = "debug-alias"
      keyPassword = "key-password"
    }
  }

  buildTypes {
    getByName("debug") {
      applicationIdSuffix = ".debug"
      signingConfig = signingConfigs.getByName("debug")
    }
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(libs.bundles.kotlin)
  implementation(libs.bundles.composeCore)
  implementation(libs.bundles.composeNavigation)
  implementation(libs.compose.paging)

  implementation(projects.components.chipLayouts)
  implementation(projects.components.row)
  implementation(projects.features.common)
  implementation(projects.features.bookmarkForm)
  implementation(projects.features.home)
  implementation(projects.features.search)
  implementation(projects.features.settings)

  implementation(libs.timber)

  implementation(libs.androidx.activityCompose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.lifecycleRuntimeKtx)

  implementation(libs.bundles.daggerRuntimes)
  kapt(libs.bundles.daggerCompilers)

  // Storage
  implementation(libs.bundles.room)
  implementation(libs.room.paging)
  kapt(libs.room.compiler)

  implementation(libs.appyx.core)

  testImplementation(libs.bundles.junitCore)

  androidTestImplementation(libs.bundles.espressoCore)
  androidTestImplementation(libs.dagger.hilt.androidTesting)
  androidTestImplementation(libs.compose.test.junit4)
  debugImplementation(libs.compose.test.manifest)
  kaptAndroidTest(libs.bundles.daggerCompilers)
}
