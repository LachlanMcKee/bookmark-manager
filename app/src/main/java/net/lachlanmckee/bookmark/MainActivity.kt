package net.lachlanmckee.bookmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.hilt.compose.navigation.factory.addNavigation
import net.lachlanmckee.hilt.compose.navigation.factory.hiltNavGraphNavigationFactories

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BookmarkApp()
    }
  }
}

@Composable
fun BookmarkApp() {
  val context = LocalContext.current
  val navController = rememberNavController()
  NavHost(navController, startDestination = "home") {
    hiltNavGraphNavigationFactories(context).addNavigation(this, navController)
  }
}
