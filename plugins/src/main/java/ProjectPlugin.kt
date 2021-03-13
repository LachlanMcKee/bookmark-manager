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
    val allDependencies = ProjectDependenciesImpl()
    moduleConfiguration.dependencies(allDependencies, this)

    allDependencies.groups.forEach { (groupName, group) ->
      group.dependencies.forEach { dependency ->
        project.dependencies.add(groupName, dependency)
      }
    }
  }

  abstract val androidPluginId: String
}
