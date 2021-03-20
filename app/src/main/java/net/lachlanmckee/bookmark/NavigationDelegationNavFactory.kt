package net.lachlanmckee.bookmark

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navigate
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.Navigation
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.compose.navigation.viewModelComposable
import javax.inject.Inject

class NavigationDelegationNavFactoryImpl @Inject constructor() : NavigationDelegationNavFactory {

  override fun <VM> create(
    viewModelClass: Class<VM>,
    builder: NavGraphBuilder,
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument>,
    deepLinks: List<NavDeepLink>,
    content: @Composable VM.(NavBackStackEntry) -> Unit
  ) where VM : ViewModel, VM : BookmarkViewModel<*, *> {

    builder.viewModelComposable(
      viewModelClass = viewModelClass,
      route = route,
      arguments = arguments,
      deepLinks = deepLinks,
      content = {
        NavigationComposable(navController, navigation)
        content(this, it)
      }
    )
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
      is Navigation.Home -> navController.navigate("home") {
        launchSingleTop = true
      }
      is Navigation.Search -> navController.navigate("search") {
        launchSingleTop = true
      }
      is Navigation.Settings -> navController.navigate("settings") {
        launchSingleTop = true
      }
    }
  }
}
