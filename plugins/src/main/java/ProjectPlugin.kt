import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

@Suppress("UnstableApiUsage")
internal abstract class ProjectPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val configurationChanged = object : ConfigurationChanged {
      override fun onConfigurationSet(moduleConfiguration: ModuleConfiguration) {
        project.applyProject(moduleConfiguration)
      }
    }
    project.extensions.create("moduleSetup", ModuleSetupExtension::class.java, configurationChanged)

    project.pluginManager.apply {
      apply(androidPluginId)
      apply("org.jetbrains.kotlin.android")
      apply("org.jetbrains.kotlin.kapt")
    }
  }

  private fun Project.applyProject(moduleConfiguration: ModuleConfiguration) {
    println("applyProject: $moduleConfiguration")

    if (this@ProjectPlugin is AppProjectPlugin) {
      extensions.configure<BaseAppModuleExtension>("android") {
        applyAndroid(moduleConfiguration)

        if (moduleConfiguration.composeEnabled) {
          buildFeatures {
            compose = true
          }
        }
      }
    }

    if (this@ProjectPlugin is LibraryProjectPlugin) {
      extensions.configure<LibraryExtension>("android") {
        applyAndroid(moduleConfiguration)

        if (moduleConfiguration.composeEnabled) {
          buildFeatures {
            compose = true
          }
        }
      }
    }

    extensions.configure<KaptExtension>("kapt") {
      correctErrorTypes = true
    }

    tasks.withType(Test::class.java) {
      useJUnitPlatform()
    }

    addDependencies(moduleConfiguration)
  }

  private fun BaseExtension.applyAndroid(moduleConfiguration: ModuleConfiguration) {
    println("applyAndroid: $moduleConfiguration")

    compileSdkVersion(30)

    defaultConfig {
      minSdkVersion(21)
      targetSdkVersion(30)

      testInstrumentationRunner =
        "net.lachlanmckee.bookmark.testing.CustomTestRunner"

      testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
      }

      testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }

    if (moduleConfiguration.composeEnabled) {
      composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
      }
    }

    packagingOptions {
      resources {
        excludes += "META-INF/AL2.0"
        excludes += "META-INF/LGPL2.1"
      }
    }
  }

  private fun Project.addDependencies(moduleConfiguration: ModuleConfiguration) {
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.coroutinesCore)
    if (moduleConfiguration.dependencies.kotlin.utils) {
      implementation(project(":utils:kotlin-utils"))
    }
    implementation(Dependencies.Logging.timber)

    moduleConfiguration.dependencies.androidX?.also { androidX ->
      implementation(Dependencies.AndroidX.activityCompose)
      implementation(Dependencies.AndroidX.appcompat)
      implementation(Dependencies.AndroidX.coreKtx)

      if (androidX.navigation) {
        implementation(Dependencies.AndroidX.navigationFragmentKtx)
        implementation(Dependencies.AndroidX.navigationUiKtx)

        if (moduleConfiguration.testingMode == ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION) {
          androidTestImplementation(EspressoTestDependencies.navigation)
        }
      }
      if (androidX.lifecycle) {
        implementation(Dependencies.AndroidX.lifecycleViewModelKtx)
        implementation(Dependencies.AndroidX.lifecycleLiveDataKtx)
      }
    }

    moduleConfiguration.dependencies.analytics?.also { analytics ->
      if (analytics.firebase) {
        implementation(Dependencies.Analytics.firebaseAnalytics)
      }
    }

    moduleConfiguration.dependencies.networking?.also { networking ->
      if (networking.okHttp) {
        implementation(Dependencies.Network.okHttp)
      }
    }

    moduleConfiguration.dependencies.dagger?.also { dagger ->
      implementation(Dependencies.Di.dagger)
      implementation(Dependencies.Di.daggerHilt)
      if (dagger == ModuleConfiguration.Dagger.WITH_COMPILER) {
        kapt(Dependencies.Di.daggerCompiler)
        kapt(Dependencies.Di.daggerHiltCompiler)

        if (moduleConfiguration.testingMode == ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION) {
          androidTestImplementation(EspressoTestDependencies.daggerHiltAndroidTesting)
          kaptAndroidTest(Dependencies.Di.daggerHiltCompiler)
        }
      }
    }

    moduleConfiguration.dependencies.compose?.also { compose ->
      implementation(Dependencies.Compose.ui)
      implementation(Dependencies.Compose.uiTooling)
      implementation(Dependencies.Compose.foundation)
      implementation(Dependencies.Compose.material)

      if (compose.icons) {
        implementation(Dependencies.Compose.iconsCore)
        implementation(Dependencies.Compose.iconsExtended)
      }
      if (compose.liveData) {
        implementation(Dependencies.Compose.liveData)
      }
      if (compose.paging) {
        implementation(Dependencies.Compose.paging)
      }
      if (compose.simpleFlowRow) {
        implementation(Dependencies.Compose.simpleFlowRow)
      }

      if (moduleConfiguration.testingMode == ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION) {
        androidTestImplementation(EspressoTestDependencies.composeTesting)
      }
    }

    moduleConfiguration.dependencies.room?.also { room ->
      implementation(Dependencies.Storage.roomRuntime)
      implementation(Dependencies.Storage.roomKtx)
      if (room == ModuleConfiguration.Room.WITH_COMPILER) {
        kapt(Dependencies.Storage.roomCompiler)
      }
    }

    moduleConfiguration.dependencies.instrumentation?.also { instrumentation ->
      require(moduleConfiguration.testingMode == ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION) {
        "testingMode must be UNIT_AND_INSTRUMENTATION if specifying instrumentation dependencies"
      }
      if (instrumentation.applitools) {
        androidTestImplementation(EspressoTestDependencies.applitoolsEspresso)
        androidTestImplementation(EspressoTestDependencies.applitoolsCommon)
        androidTestImplementation(EspressoTestDependencies.applitoolsCore)
        androidTestImplementation(EspressoTestDependencies.applitoolsComponents)
        androidTestImplementation(EspressoTestDependencies.applitoolsComponentsAndroidX)

        if (moduleConfiguration.composeEnabled) {
          implementation(project(":utils:applitools-compose"))
        }
      }
    }

    if (moduleConfiguration.testingMode != ModuleConfiguration.TestingMode.NONE) {
      testImplementation(UnitTestDependencies.junitEngine)
      testImplementation(UnitTestDependencies.junitApi)
      testImplementation(UnitTestDependencies.mockk)
      testImplementation(UnitTestDependencies.coroutinesTest)
    }

    if (moduleConfiguration.testingMode == ModuleConfiguration.TestingMode.UNIT_AND_INSTRUMENTATION) {
      debugImplementation(EspressoTestDependencies.fragmentTesting)
      androidTestImplementation(EspressoTestDependencies.junit)
      androidTestImplementation(EspressoTestDependencies.espressoCore)
      androidTestImplementation(EspressoTestDependencies.espressoIntents)
      androidTestImplementation(EspressoTestDependencies.runner)
      androidTestImplementation(EspressoTestDependencies.rules)
      androidTestImplementation(EspressoTestDependencies.mockk)

      androidTestUtil(EspressoTestDependencies.orchestrator)
    }
  }

  private fun Project.implementation(dependencyNotation: Any) {
    project.dependencies.add("implementation", dependencyNotation)
  }

  private fun Project.debugImplementation(dependencyNotation: Any) {
    project.dependencies.add("debugImplementation", dependencyNotation)
  }

  private fun Project.kapt(dependencyNotation: Any) {
    project.dependencies.add("kapt", dependencyNotation)
  }

  private fun Project.testImplementation(dependencyNotation: Any) {
    project.dependencies.add("testImplementation", dependencyNotation)
  }

  private fun Project.androidTestImplementation(dependencyNotation: Any) {
    project.dependencies.add("androidTestImplementation", dependencyNotation)
  }

  private fun Project.kaptAndroidTest(dependencyNotation: Any) {
    project.dependencies.add("kaptAndroidTest", dependencyNotation)
  }

  private fun Project.androidTestUtil(dependencyNotation: Any) {
    project.dependencies.add("androidTestUtil", dependencyNotation)
  }

  abstract val androidPluginId: String
}
