package net.lachlanmckee.bookmark.testing.util.annotation

import android.os.Build
import androidx.test.internal.runner.filters.ParentFilter
import org.junit.runner.Description

@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.CLASS,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplitoolsTest {
  class ApplitoolsFilter : ParentFilter() {
    override fun evaluateTest(description: Description): Boolean {
      val testAnnotation = description.getAnnotation(
        ApplitoolsTest::class.java
      )
      val classAnnotation = description.testClass?.getAnnotation(
        ApplitoolsTest::class.java
      )
      return isApplitoolsSupported(testAnnotation) && isApplitoolsSupported(classAnnotation)
    }

    override fun describe(): String {
      return "Skip tests annotated with 'ApplitoolsTest' if the OS is lower than API 26 " +
        "(due to Jetpack Compose not supporting screenshots before this version)"
    }

    private fun isApplitoolsSupported(annotation: ApplitoolsTest?): Boolean {
      return if (annotation != null) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
      } else {
        true
      }
    }
  }
}
