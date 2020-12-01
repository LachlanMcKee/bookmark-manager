buildscript {
  repositories {
    google()
    jcenter()
  }
}

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("com.google.gms.google-services")
  id("dagger.hilt.android.plugin")
  id("com.google.firebase.appdistribution")
  id("androidx.navigation.safeargs.kotlin")
}

android {
  compileSdkVersion(30)

  defaultConfig {
    applicationId = "net.lachlanmckee.bookmark"
    minSdkVersion(21)
    targetSdkVersion(30)
    versionCode = System.getenv("BITRISE_BUILD_NUMBER")?.toIntOrNull() ?: 1
    versionName = "0.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    testOptions {
      execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    testInstrumentationRunnerArguments["clearPackageData"] = "true"
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

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerVersion = Dependencies.Kotlin.version
    kotlinCompilerExtensionVersion = Dependencies.Compose.version
  }
}

kapt {
  correctErrorTypes = true
}

tasks.withType<Test> {
  useJUnitPlatform()
}

dependencies {
  implementation(Dependencies.Kotlin.stdlib)

  implementation(Dependencies.AndroidX.appcompat)
  implementation(Dependencies.AndroidX.coreKtx)
  implementation(Dependencies.AndroidX.constraintLayout)
  implementation(Dependencies.AndroidX.lifecycleExtensions)
  implementation(Dependencies.AndroidX.lifecycleViewModelKtx)
  implementation(Dependencies.AndroidX.lifecycleLiveDataKtx)
  implementation(Dependencies.AndroidX.navigationFragmentKtx)
  implementation(Dependencies.AndroidX.navigationUiKtx)

  implementation(Dependencies.Compose.ui)
  implementation(Dependencies.Compose.uiTooling)
  implementation(Dependencies.Compose.foundation)
  implementation(Dependencies.Compose.material)
  implementation(Dependencies.Compose.iconsCore)
  implementation(Dependencies.Compose.iconsExtended)
  implementation(Dependencies.Compose.liveData)

  // Storage
  implementation(Dependencies.Storage.roomRuntime)
  kapt(Dependencies.Storage.roomCompiler)
  implementation(Dependencies.Storage.roomKtx)

  implementation(Dependencies.Network.okHttp)
  implementation(Dependencies.Analytics.firebaseAnalytics)

  implementation(Dependencies.Di.dagger)
  implementation(Dependencies.Di.daggerHilt)
  kapt(Dependencies.Di.daggerCompiler)
  kapt(Dependencies.Di.daggerHiltCompiler)

  implementation(Dependencies.Kotlin.coroutinesCore)

  implementation(Dependencies.Logging.timber)

  testImplementation(UnitTestDependencies.junitEngine)
  testImplementation(UnitTestDependencies.junitApi)
  testImplementation(UnitTestDependencies.mockk)
  testImplementation(UnitTestDependencies.coroutinesTest)

  debugImplementation(EspressoTestDependencies.fragmentTesting)
  androidTestImplementation(EspressoTestDependencies.junit)
  androidTestImplementation(EspressoTestDependencies.espressoCore)
  androidTestImplementation(EspressoTestDependencies.espressoIntents)
  androidTestImplementation(EspressoTestDependencies.runner)
  androidTestImplementation(EspressoTestDependencies.rules)
  androidTestImplementation(EspressoTestDependencies.composeTesting)
  androidTestImplementation(EspressoTestDependencies.navigation)
  androidTestImplementation(EspressoTestDependencies.daggerHiltAndroidTesting)
  kaptAndroidTest(Dependencies.Di.daggerHiltCompiler)
  androidTestUtil(EspressoTestDependencies.orchestrator)
}
