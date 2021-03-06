import net.lachlanmckee.bookmark.ModuleConfiguration
import java.util.Properties

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

    addOptionalKey("APPLITOOLS_API_KEY", getApplitoolsApiKey())
    addOptionalKey("APPLITOOLS_BATCH_ID", System.getenv("APPLITOOLS_BATCH_ID"))
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

fun getApplitoolsApiKey(): String? {
  var applitoolsApiKey: String? = System.getenv("APPLITOOLS_API_KEY")
  if (applitoolsApiKey == null) {
    val propsFile = file("../local.properties")
    if (propsFile.exists()) {
      applitoolsApiKey = Properties().run {
        load(propsFile.reader())
        getProperty("applitoolsApiKey")
      }
    }
  }
  return applitoolsApiKey
}

fun com.android.build.api.dsl.BaseFlavor.addOptionalKey(key: String, value: String?) {
  if (value != null) {
    buildConfigField("String", key, "\"$value\"")
  } else {
    buildConfigField("String", key, "null")
  }
}

dependencies {
  implementation(project(":components:chip-layouts"))
  implementation(project(":components:row"))
}
