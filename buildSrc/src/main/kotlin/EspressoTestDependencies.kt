object EspressoTestDependencies {
  private const val espressoVersion = "3.3.0"
  const val junit = "androidx.test.ext:junit:1.1.2"
  const val composeTesting = "androidx.ui:ui-test:${Dependencies.Compose.version}"
  const val daggerHiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Dependencies.Di.version}-alpha"
  const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
  const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"
  const val fragmentTesting = "androidx.fragment:fragment-testing:1.3.0-beta01"
  const val runner = "androidx.test:runner:1.3.0"
  const val rules = "androidx.test:rules:1.3.0"
  const val navigation = "androidx.navigation:navigation-testing:${Dependencies.AndroidX.navigationVersion}"
  const val orchestrator = "androidx.test:orchestrator:1.3.0"
}
