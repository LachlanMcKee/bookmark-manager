import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    // TODO: Once gradle adds support for accessing libs within build script, this can be removed.
    val libs = project.extensions
      .getByType<VersionCatalogsExtension>()
      .named("libs") as org.gradle.accessors.dm.LibrariesForLibs

    classpath(libs.plugin.androidTools)
    classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
    classpath(libs.plugin.hiltAndroidGradle)
  }
}

plugins {
  id("com.diffplug.spotless") version "5.15.0"
  id("com.github.ben-manes.versions") version "0.39.0"
}

spotless {
  format("misc") {
    target("*.md", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
  kotlin {
    ktlint("0.39.0").userData(
      mapOf(
        "indent_size" to "2",
        "disabled_rules" to "no-wildcard-imports"
      )
    )
    target("**/*.kt")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**", "**/GeneratedLibraries.kt")
  }
  kotlinGradle {
    ktlint("0.39.0").userData(mapOf("indent_size" to "2"))
    target("**/*.gradle.kts")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**")
  }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java)

allprojects {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}

val rootDir = projectDir
val subprojectLibs = libs

subprojects {
  buildscript {
    repositories {
      google()
      mavenCentral()
    }

    dependencies {
      classpath(subprojectLibs.plugin.karumiShot)
    }
  }

  repositories {
    google()
    mavenCentral()
  }

  pluginManager.withPlugin("java") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "1.8"
      useIR = true
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xopt-in=kotlin.ExperimentalStdlibApi",
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.contracts.ExperimentalContracts",
        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
      )
    }
  }

  configurations.all {
    resolutionStrategy {
      eachDependency {
        when (requested.module.toString()) {
          "androidx.compose.runtime:runtime" -> useVersion(libs.versions.compose.get())
          "androidx.compose.ui:ui" -> useVersion(libs.versions.compose.get())
        }
      }
    }
  }
}
