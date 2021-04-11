object Dependencies {
  object AndroidX {
    const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha06"
    const val appcompat = "androidx.appcompat:appcompat:1.3.0-rc01"
    const val coreKtx = "androidx.core:core-ktx:1.5.0-rc01"
    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01"
  }

  object Compose {
    const val version = "1.0.0-beta04"
    const val ui = "androidx.compose.ui:ui:$version"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
    const val foundation = "androidx.compose.foundation:foundation:$version"
    const val material = "androidx.compose.material:material:$version"
    const val iconsCore = "androidx.compose.material:material-icons-core:$version"
    const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
    const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
    const val paging = "androidx.paging:paging-compose:1.0.0-alpha08"
    const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha10"
    const val flowLayout = "com.google.accompanist:accompanist-flowlayout:0.7.1"
  }

  object Di {
    const val version = "2.34"
    const val dagger = "com.google.dagger:dagger:$version"
    const val daggerHilt = "com.google.dagger:hilt-android:$version-beta"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$version"
    const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:$version-beta"
    const val daggerHiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha01"
    const val composeNavigationFactory =
      "net.lachlanmckee:hilt-compose-navigation-factory:1.0.0-alpha01"
    const val composeNavigationFactoryCompiler =
      "net.lachlanmckee:hilt-compose-navigation-factory-compiler:1.0.0-alpha01"
  }

  object Storage {
    private const val roomVersion = "2.3.0-rc01"
    const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomKtx = "androidx.room:room-ktx:$roomVersion"
  }

  object Kotlin {
    const val version = "1.4.32"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
  }

  object Logging {
    const val timber = "com.jakewharton.timber:timber:4.7.1"
  }

  object Network {
    const val okHttp = "com.squareup.okhttp3:okhttp:4.9.0"
  }
}
