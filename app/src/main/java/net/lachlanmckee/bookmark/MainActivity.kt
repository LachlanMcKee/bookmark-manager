package net.lachlanmckee.bookmark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.Navigation
import net.lachlanmckee.bookmark.feature.home.HomeScreen
import net.lachlanmckee.bookmark.feature.home.HomeViewModelImpl
import net.lachlanmckee.bookmark.feature.search.SearchScreen
import net.lachlanmckee.bookmark.feature.search.SearchViewModelImpl
import net.lachlanmckee.bookmark.feature.settings.SettingsScreen
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
      bookmarkComposable<HomeViewModelImpl>(
        navController = navController,
        route = "home",
        content = {
          HomeScreen(
            stateLiveData = state,
            events = eventConsumer
          )
        }
      )
      bookmarkComposable<SearchViewModelImpl>(
        navController = navController,
        route = "search",
        content = {
          SearchScreen(
            stateLiveData = state,
            events = eventConsumer
          )
        }
      )
      bookmarkComposable<SettingsViewModel>(
        navController = navController,
        route = "settings",
        content = {
          SettingsScreen(this)
        }
      )
    }
  }

  inline fun <reified VM> NavGraphBuilder.bookmarkComposable(
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable VM.(NavBackStackEntry) -> Unit
  ) where VM : ViewModel, VM : BookmarkViewModel<*, *> {
    composable(route, arguments, deepLinks) {
      val viewModel: VM = hiltNavGraphViewModel()
      NavigationComposable(navController, viewModel.navigation)

      content(viewModel, it)
    }
  }

  @Composable
  fun NavigationComposable(
    navController: NavHostController,
    navigationLiveData: LiveData<Navigation>
  ) {
    val context = LocalContext.current
    val navigation: Navigation? by navigationLiveData.observeAsState()

    if (navigation != null) {
      when (val nonNullNavigation = navigation) {
        is Navigation.Back -> navController.popBackStack()
        is Navigation.Bookmark -> context.startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(nonNullNavigation.url))
        )
        is Navigation.Home -> navController.navigate("home") { launchSingleTop = true }
        is Navigation.Search -> navController.navigate("search") { launchSingleTop = true }
        is Navigation.Settings -> navController.navigate("settings") { launchSingleTop = true }
      }
    }
  }
}
