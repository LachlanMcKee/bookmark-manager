package net.lachlanmckee.bookmark

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.model.Navigation
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

    builder.composable(route, arguments, deepLinks) {
      // Logic copied from hiltNavGraphViewModel for now as they do not provide a non-reified version.
      val owner = LocalViewModelStoreOwner.current
      val viewModel: VM =
        if (owner is NavBackStackEntry) {
          val viewModelFactory = HiltViewModelFactory(
            context = LocalContext.current,
            navBackStackEntry = owner
          )
          ViewModelProvider(owner, viewModelFactory).get(viewModelClass)
        } else {
          viewModel(viewModelClass)
        }

      NavigationComposable(navController, viewModel.navigation)
      content(viewModel, it)
    }
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
          val canPopBackstack = navController.currentDestination?.route != "home"
          if (!canPopBackstack || !navController.popBackStack()) {
            closeActivity(context)
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
          popUpTo("home")
        }
        is Navigation.Settings -> navController.navigate("settings") {
          launchSingleTop = true
          popUpTo("home")
        }
        is Navigation.AddBookmark -> navController.navigate("bookmark-form")
      }
    }
  }
}

private fun closeActivity(context: Context) {
  context
    .let {
      var ctx = it
      while (ctx is ContextWrapper) {
        if (ctx is Activity) {
          return@let ctx
        }
        ctx = ctx.baseContext
      }
      throw IllegalStateException(
        "Expected an activity context but instead found: $ctx"
      )
    }
    .finish()
}
