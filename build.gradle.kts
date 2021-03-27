import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.0-alpha10")
    classpath(kotlin("gradle-plugin", version = "1.4.31"))

    classpath("com.google.gms:google-services:4.3.4")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.33-beta")
    classpath("com.google.firebase:firebase-appdistribution-gradle:2.0.1")
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
    ktlint("0.39.0").userData(
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
    ktlint("0.39.0").userData(mapOf("indent_size" to "2"))
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
    maven(url = "https://jitpack.io")
  }
}

val rootDir = projectDir

subprojects {
  buildscript {
    repositories {
      google()
      jcenter()
    }

    dependencies {
      classpath("com.karumi:shot:5.10.3")
    }
  }

  repositories {
    google()
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
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xopt-in=kotlin.ExperimentalStdlibApi",
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.contracts.ExperimentalContracts",
        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
      )
    }
  }

  tasks.register("dependenciesUpdateCheck", Exec::class.java) {
    dependenciesUpdateFunc { newDependencies ->
      val existingDependencies = project.file("dependencies-baseline.txt").readText()
      if (existingDependencies != newDependencies) {
        project.file("dependencies-new.txt").writeText(newDependencies)
        throw GradleException("Dependencies have been updated")
      }
    }
  }

  tasks.register("dependenciesUpdateApply", Exec::class.java) {
    dependenciesUpdateFunc { newDependencies ->
      project.file("dependencies-baseline.txt").writeText(newDependencies)
    }
  }
}

inline fun Exec.dependenciesUpdateFunc(crossinline func: Task.(String) -> Unit) {
  enabled = project.tasks.any { it.name == "androidDependencies" }
  workingDir = rootDir

  if (System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT).contains("windows")) {
    commandLine("cmd", "/c", "gradlew.bat", "${project.path}:androidDependencies")
  } else {
    commandLine("./gradlew", "${project.path}:androidDependencies")
  }

  val taskOutput = StringBuilder()
  var acceptOutput = false
  val listener: StandardOutputListener = object : StandardOutputListener {
    override fun onOutput(line: CharSequence) {
      if (acceptOutput) {
        if (line.startsWith("BUILD SUCCESSFUL")) {
          acceptOutput = false
          logging.removeStandardErrorListener(this)
        } else {
          taskOutput.append(line)
        }
      } else {
        acceptOutput = line.startsWith("> Task ${project.path}:androidDependencies")
      }
    }
  }

  logging.addStandardOutputListener(listener)
  doLast {
    logging.removeStandardErrorListener(listener)
    func(taskOutput.toString())
  }
}
