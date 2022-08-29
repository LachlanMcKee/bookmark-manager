import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation(libs.plugin.androidTools)
  implementation(libs.plugin.kotlinGradle)
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
        const val ComposeCompiler = "${libs.versions.composeCompiler.get()}"
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
