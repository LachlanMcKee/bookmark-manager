package net.lachlanmckee.bookmark.dependencies

internal object Dependencies {
  object Analytics {
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics:17.6.0"
  }
  object AndroidX {
    private const val lifecycleVersion = "2.2.0"
    const val navigationVersion = "2.3.3"
    const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha03"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
  }
  object Compose {
    const val version = "1.0.0-beta01"
    const val ui = "androidx.compose.ui:ui:$version"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
    const val foundation = "androidx.compose.foundation:foundation:$version"
    const val material = "androidx.compose.material:material:$version"
    const val iconsCore = "androidx.compose.material:material-icons-core:$version"
    const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
    const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
    const val paging = "androidx.paging:paging-compose:1.0.0-alpha08"
    const val simpleFlowRow = "com.github.LachlanMcKee:android-samples:1.0.0"
  }
  object Di {
    const val version = "2.31.2"
    const val dagger = "com.google.dagger:dagger:$version"
    const val daggerHilt = "com.google.dagger:hilt-android:$version-alpha"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$version"
    const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:$version-alpha"
  }
  object Storage {
    private const val roomVersion = "2.3.0-alpha04"
    const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomKtx = "androidx.room:room-ktx:$roomVersion"
  }
  object Kotlin {
    const val version = "1.4.30"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"
  }
  object Logging {
    const val timber = "com.jakewharton.timber:timber:4.7.1"
  }
  object Network {
    const val okHttp = "com.squareup.okhttp3:okhttp:4.9.0"
  }
}
