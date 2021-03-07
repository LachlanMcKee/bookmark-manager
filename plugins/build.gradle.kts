buildscript {
  repositories {
    google()
    jcenter()
  }
}

plugins {
  kotlin("jvm") version "1.4.30"
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  google()
  jcenter()
}

dependencies {
  implementation("com.android.tools.build:gradle:7.0.0-alpha08")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
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
