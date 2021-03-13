import org.gradle.api.Project

object CommonDependencies {
  class ComposeCore(project: Project) : ProjectDependencies {
    private val impl = ProjectDependenciesImpl().apply {
      implementation(
        Dependencies.Kotlin.stdlib,
        Dependencies.Kotlin.coroutinesCore,
        project.project(":utils:kotlin-utils"),

        Dependencies.Compose.ui,
        Dependencies.Compose.uiTooling,
        Dependencies.Compose.foundation,
        Dependencies.Compose.material
      )

      testImplementation(
        UnitTestDependencies.junitEngine,
        UnitTestDependencies.junitApi,
        UnitTestDependencies.mockk,
        UnitTestDependencies.coroutinesTest
      )

      androidTestImplementation(
        EspressoTestDependencies.junit,
        EspressoTestDependencies.espressoCore,
        EspressoTestDependencies.runner,
        EspressoTestDependencies.rules,
        EspressoTestDependencies.composeTesting
      )

      androidTestUtil(EspressoTestDependencies.orchestrator)
    }

    override val groups: Map<String, DependencyGroup>
      get() = impl.groups

    override fun group(value: String): DependencyGroup {
      return impl.group(value)
    }

    override fun appendFrom(projectDependencies: ProjectDependencies) {
      return impl.appendFrom(projectDependencies)
    }
  }
}
