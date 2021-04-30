object EspressoTestDependencies {
  private const val espressoVersion = "3.3.0"
  const val junit = "androidx.test.ext:junit:1.1.2"
  const val composeTesting = "androidx.compose.ui:ui-test-junit4:${Dependencies.Compose.version}"
  const val daggerHiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Dependencies.Di.version}"
  const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
  const val runner = "androidx.test:runner:1.3.0"
  const val rules = "androidx.test:rules:1.3.0"
  const val orchestrator = "androidx.test:orchestrator:1.3.0"
  const val mockk = "io.mockk:mockk-android:1.11.0"
}
