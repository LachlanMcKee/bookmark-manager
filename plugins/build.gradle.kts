import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

buildscript {
  repositories {
    google()
    jcenter()
  }
}

plugins {
  kotlin("jvm") version "1.4.32"
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  google()
  jcenter()
}

dependencies {
  implementation("com.android.tools.build:gradle:7.0.0-alpha15")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}

gradlePlugin {
  plugins {
    create("net.lachlanmckee.bookmark.app") {
      id = "net.lachlanmckee.bookmark.app"
      implementationClass = "AppProjectPlugin"
    }
    create("net.lachlanmckee.bookmark.library") {
      id = "net.lachlanmckee.bookmark.library"
      implementationClass = "LibraryProjectPlugin"
    }
  }
}

tasks.register("createLibrary") {
  val orchestrator = libs.espresso.orchestrator.get()
  File(projectDir, "src/main/java/GeneratedLibraries.kt")
    .writeText(
      """
      object Versions {
        const val Compose = "${libs.versions.compose.get()}"
      }
      object Dependencies {
        const val Orchestrator = "${orchestrator.module.group}:${orchestrator.module.name}:${orchestrator.versionConstraint.requiredVersion}"
      }

      """.trimIndent()
    )
}

tasks.forEach {
  if (it.name != "createLibrary" && it.name != "clean") {
    it.dependsOn(":createLibrary")
  }
}
