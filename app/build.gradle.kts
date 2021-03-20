plugins {
  id("net.lachlanmckee.bookmark.app")
  id("com.google.gms.google-services")
  id("dagger.hilt.android.plugin")
  id("com.google.firebase.appdistribution")
}

moduleSetup {
  configuration = ModuleConfiguration(
    composeEnabled = true,
    useHiltWithinAndroidTest = true,
    dependencies = { project ->
      appendFrom(CommonDependencies.ComposeCore(project))

      implementation(
        project(":components:chip-layouts"),
        project(":components:row"),
        project(":features:common"),
        project(":features:home"),

        Dependencies.Logging.timber,

        Dependencies.AndroidX.activityCompose,
        Dependencies.AndroidX.appcompat,
        Dependencies.AndroidX.coreKtx,
        Dependencies.AndroidX.lifecycleViewModelKtx,
        Dependencies.AndroidX.lifecycleLiveDataKtx,

        Dependencies.Analytics.firebaseAnalytics,

        Dependencies.Network.okHttp
      )

      implementation(Dependencies.Di.dagger)
      implementation(Dependencies.Di.daggerHilt)
      kapt(Dependencies.Di.daggerCompiler)
      kapt(Dependencies.Di.daggerHiltCompiler)
      implementation(Dependencies.Di.daggerHiltNavigationCompose)

      implementation(
        Dependencies.Compose.iconsCore,
        Dependencies.Compose.iconsExtended,
        Dependencies.Compose.liveData,
        Dependencies.Compose.paging,
        Dependencies.Compose.simpleFlowRow,
        Dependencies.Compose.navigation
      )

      // Storage
      implementation(Dependencies.Storage.roomRuntime)
      kapt(Dependencies.Storage.roomCompiler)
      implementation(Dependencies.Storage.roomKtx)

      androidTestImplementation(EspressoTestDependencies.daggerHiltAndroidTesting)
      kaptAndroidTest(Dependencies.Di.daggerHiltCompiler)
    }
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
