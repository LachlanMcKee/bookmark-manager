package net.lachlanmckee.bookmark

import javax.inject.Inject

open class ModuleSetupExtension @Inject constructor(
  private val configurationChanged: ConfigurationChanged
) {
  var configuration: ModuleConfiguration? = null
    set(value) {
      field = value
      if (value != null) {
        configurationChanged.onConfigurationSet(value)
      }
    }
}

interface ConfigurationChanged {
  fun onConfigurationSet(moduleConfiguration: ModuleConfiguration)
}

data class ModuleConfiguration(
  val testingMode: TestingMode,
  val dependencies: Dependencies,
  val composeEnabled: Boolean = false
) {
  companion object {
    fun composeModule(compose: Compose.() -> Unit = {}) = ModuleConfiguration(
      composeEnabled = true,
      testingMode = TestingMode.UNIT_AND_INSTRUMENTATION,
      dependencies = Dependencies(
        compose = Compose().apply(compose)
      )
    )
  }

  data class Dependencies(
    val kotlin: Kotlin = Kotlin(),
    val androidX: AndroidX? = null,
    val analytics: Analytics? = null,
    val networking: Networking? = null,
    val dagger: Dagger? = null,
    val compose: Compose? = null,
    val room: Room? = null,
    val instrumentation: Instrumentation? = null
  )

  data class Kotlin(
    val utils: Boolean = true
  )

  data class AndroidX(
    val navigation: Boolean = false,
    val lifecycle: Boolean = false
  ) {
    companion object {
      val CORE_ONLY = AndroidX()
    }
  }

  data class Analytics(
    val firebase: Boolean = false
  )

  data class Networking(
    val okHttp: Boolean = false
  )

  enum class Dagger {
    WITH_COMPILER, WITHOUT_COMPILER
  }

  data class Compose(
    var icons: Boolean = false,
    var liveData: Boolean = false,
    var paging: Boolean = false,
    var simpleFlowRow: Boolean = false
  ) {
    companion object {
      val CORE_ONLY = Compose()
      val ENABLE_ALL = Compose(
        icons = true,
        liveData = true,
        paging = true,
        simpleFlowRow = true
      )
    }
  }

  enum class Room {
    WITH_COMPILER, WITHOUT_COMPILER
  }

  enum class TestingMode {
    NONE, UNIT, UNIT_AND_INSTRUMENTATION
  }

  data class Instrumentation(
    val applitools: Boolean = false
  )
}
