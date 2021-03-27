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
        EspressoTestDependencies.composeTesting,
        project.project(":utils:instrumentation-utils")
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

  class FeatureCore(project: Project) : ProjectDependencies {
    private val impl = ProjectDependenciesImpl().apply {
      appendFrom(ComposeCore(project))

      implementation(
        project.project(":features:common"),
        project.project(":utils:compose-navigation"),

        Dependencies.AndroidX.activityCompose,
        Dependencies.AndroidX.lifecycleViewModelKtx,
        Dependencies.AndroidX.lifecycleLiveDataKtx,
        Dependencies.Compose.liveData,
        Dependencies.Di.dagger,
        Dependencies.Di.daggerHilt
      )
      kapt(
        Dependencies.Di.daggerCompiler,
        Dependencies.Di.daggerHiltCompiler
      )
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
