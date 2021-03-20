interface ProjectDependencies {
  val groups: Map<String, DependencyGroup>
  fun group(value: String): DependencyGroup
  fun appendFrom(projectDependencies: ProjectDependencies)
}

class ProjectDependenciesImpl : ProjectDependencies {
  override val groups: MutableMap<String, DependencyGroup> = mutableMapOf()

  override fun group(value: String): DependencyGroup {
    val group = groups[value]
    if (group != null) {
      return group
    }
    return DependencyGroup().apply { groups[value] = this }
  }

  override fun appendFrom(projectDependencies: ProjectDependencies) {
    projectDependencies.groups.forEach { (key, group) ->
      if (groups.containsKey(key)) {
        groups.getValue(key).dependencies.addAll(group.dependencies)
      } else {
        groups[key] = DependencyGroup().apply { dependencies.addAll(group.dependencies) }
      }
    }
  }
}

class DependencyGroup {
  val dependencies: MutableSet<Any> = mutableSetOf()

  fun add(dependency: Any) {
    dependencies.add(dependency)
  }
}

fun ProjectDependencies.api(vararg dependencies: Any) =
  dependencyGroup("api", *dependencies)

fun ProjectDependencies.implementation(vararg dependencies: Any) =
  dependencyGroup("implementation", *dependencies)

fun ProjectDependencies.debugImplementation(vararg dependencies: Any) =
  dependencyGroup("debugImplementation", *dependencies)

fun ProjectDependencies.kapt(vararg dependencies: Any) =
  dependencyGroup("kapt", *dependencies)

fun ProjectDependencies.testImplementation(vararg dependencies: Any) =
  dependencyGroup("testImplementation", *dependencies)

fun ProjectDependencies.androidTestImplementation(vararg dependencies: Any) =
  dependencyGroup("androidTestImplementation", *dependencies)

fun ProjectDependencies.kaptAndroidTest(vararg dependencies: Any) =
  dependencyGroup("kaptAndroidTest", *dependencies)

fun ProjectDependencies.androidTestUtil(vararg dependencies: Any) =
  dependencyGroup("androidTestUtil", *dependencies)

fun ProjectDependencies.androidTestRuntimeOnly(vararg dependencies: Any) =
  dependencyGroup("androidTestRuntimeOnly", *dependencies)

private fun ProjectDependencies.dependencyGroup(type: String, vararg dependencies: Any) {
  group(type).also { group ->
    dependencies.forEach { dependency ->
      group.add(dependency)
    }
  }
}

private fun ProjectDependencies.dependencyGroup(
  type: String,
  vararg dependencyGroups: DependencyGroup
) {
  group(type).also { group ->
    dependencyGroups.forEach { existingGroup ->
      existingGroup.dependencies.forEach { dependency ->
        group.add(dependency)
      }
    }
  }
}
