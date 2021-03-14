import org.gradle.api.Project
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
  val composeEnabled: Boolean = false,
  val useHiltWithinAndroidTest: Boolean = false,
  val dependencies: (ProjectDependencies.(Project) -> Unit) = {}
)
