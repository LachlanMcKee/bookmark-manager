package net.lachlanmckee.bookmark.testing

import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.MainActivity
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {
  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @get:Rule
  val activityRule = ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun verifyHomeScreenLaunched() {
    activityRule.scenario
  }
}
