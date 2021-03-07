plugins {
  id("net.lachlanmckee.bookmark.app")
  id("com.google.gms.google-services")
  id("dagger.hilt.android.plugin")
  id("com.google.firebase.appdistribution")
  id("androidx.navigation.safeargs.kotlin")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    testingMode = ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION,
    dependencies = ModuleConfiguration.Dependencies(
      androidX = ModuleConfiguration.AndroidX(
        navigation = true,
        lifecycle = true
      ),
      analytics = ModuleConfiguration.Analytics(
        firebase = true
      ),
      networking = ModuleConfiguration.Networking(
        okHttp = true
      ),
      dagger = ModuleConfiguration.Dagger.WITH_COMPILER,
      compose = ModuleConfiguration.Compose.ENABLE_ALL,
      room = ModuleConfiguration.Room.WITH_COMPILER,
      instrumentation = ModuleConfiguration.Instrumentation(
        applitools = true
      )
    )
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
  implementation(project(":components:chip-layouts"))
  implementation(project(":components:row"))
}
