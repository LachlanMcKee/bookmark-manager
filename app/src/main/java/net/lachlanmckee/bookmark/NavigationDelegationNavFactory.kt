package net.lachlanmckee.bookmark

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navigate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.compose.navigation.isCurrentRoute
import net.lachlanmckee.compose.navigation.popUpToRoute
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
  navigationFlow: Flow<Navigation>
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val navigationFlowLifecycleAware = remember(navigationFlow, lifecycleOwner) {
    navigationFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
  }
  LaunchedEffect(navigationFlowLifecycleAware) {
    navigationFlowLifecycleAware.collectLatest { navigation ->
      when (navigation) {
        is Navigation.Back -> {
          val canPopBackstack = !navController.isCurrentRoute("home")
          if (canPopBackstack) {
            navController.popBackStack()
          }
        }
        is Navigation.Bookmark -> {
          context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(navigation.url))
          )
        }
        is Navigation.Home -> navController.navigate("home") {
          launchSingleTop = true
        }
        is Navigation.Search -> navController.navigate("search") {
          launchSingleTop = true
          popUpToRoute("home")
        }
        is Navigation.Settings -> navController.navigate("settings") {
          launchSingleTop = true
          popUpToRoute("home")
        }
        is Navigation.AddBookmark -> navController.navigate("bookmark-form")
      }
    }
  }
}
