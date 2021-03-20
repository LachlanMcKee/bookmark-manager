package net.lachlanmckee.bookmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.compose.navigation.ComposeNavigationFactory
import net.lachlanmckee.compose.navigation.addNavigation
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var composeNavigationFactories: @JvmSuppressWildcards Set<ComposeNavigationFactory>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BookmarkApp()
    }
  }

  @Composable
  fun BookmarkApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
      composeNavigationFactories.addNavigation(this, navController)
    }
  }
}
