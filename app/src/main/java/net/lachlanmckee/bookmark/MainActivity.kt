package net.lachlanmckee.bookmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.hilt.compose.navigation.factory.addNavigation
import net.lachlanmckee.hilt.compose.navigation.factory.hiltNavGraphNavigationFactories

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme {
        BookmarkApp()
      }
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BookmarkApp() {
  val context = LocalContext.current
  val navController = rememberAnimatedNavController()
  AnimatedNavHost(navController, startDestination = "home") {
    hiltNavGraphNavigationFactories(context).addNavigation(this, navController)
  }
}
