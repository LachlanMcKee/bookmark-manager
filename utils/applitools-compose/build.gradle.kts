import java.util.Properties

plugins {
  id("net.lachlanmckee.bookmark.library")
}

moduleSetup {
  configuration = ModuleConfiguration(
    testingMode = ModuleConfiguration.TestingMode.NONE,
    dependencies = ModuleConfiguration.Dependencies(
      compose = ModuleConfiguration.Compose()
    ),
    composeEnabled = true
  )
}

android {
  defaultConfig {
    addOptionalKey("APPLITOOLS_API_KEY", getApplitoolsApiKey())
    addOptionalKey("APPLITOOLS_BATCH_ID", System.getenv("APPLITOOLS_BATCH_ID"))
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
  implementation(EspressoTestDependencies.runner)
  implementation(EspressoTestDependencies.applitoolsEspresso)
  implementation(EspressoTestDependencies.applitoolsCommon)
  implementation(EspressoTestDependencies.applitoolsCore)
  implementation(EspressoTestDependencies.applitoolsComponents)
  implementation(EspressoTestDependencies.applitoolsComponentsAndroidX)
  implementation(EspressoTestDependencies.composeTesting)
}
