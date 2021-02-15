import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.0-alpha07")
    classpath(kotlin("gradle-plugin", version = Dependencies.Kotlin.version))

    classpath("com.google.gms:google-services:4.3.4")
    classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.Di.version}-alpha")
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.0.1")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Dependencies.AndroidX.navigationVersion}")
  }
}

plugins {
  id("com.diffplug.spotless") version "5.7.0"
  id("com.github.ben-manes.versions") version "0.33.0"
}

spotless {
  format("misc") {
    target("*.md", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
  kotlin {
    ktlint(Dependencies.ktlintVersion).userData(
      mapOf(
        "indent_size" to "2",
        "disabled_rules" to "no-wildcard-imports"
      )
    )
    target("**/*.kt")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**")
  }
  kotlinGradle {
    ktlint(Dependencies.ktlintVersion).userData(mapOf("indent_size" to "2"))
    target("**/*.gradle.kts")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**")
  }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
  rejectVersionIf {
    candidate.version.contains("alpha") ||
      candidate.version.contains("beta") ||
      candidate.version.contains("RC") ||
      candidate.version.contains("M[0-9]+?$".toRegex())
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}

subprojects {
  repositories {
    jcenter()
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
      freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
      freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.contracts.ExperimentalContracts"
    }
  }
}
