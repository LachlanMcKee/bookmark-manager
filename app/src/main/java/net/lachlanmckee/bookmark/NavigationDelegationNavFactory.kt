package net.lachlanmckee.bookmark

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.Navigation
import javax.inject.Inject

interface NavigationDelegationNavFactory {
  fun <VM> create(
    viewModelClass: Class<VM>,
    builder: NavGraphBuilder,
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable VM.(NavBackStackEntry) -> Unit
  ) where VM : ViewModel, VM : BookmarkViewModel<*, *>
}

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
    builder.bookmarkComposable(
      viewModelClass = viewModelClass,
      navController = navController,
      route = route,
      arguments = arguments,
      deepLinks = deepLinks,
      content = content
    )
  }
}

inline fun <reified VM> NavigationDelegationNavFactory.create(
  builder: NavGraphBuilder,
  navController: NavHostController,
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  noinline content: @Composable VM.(NavBackStackEntry) -> Unit
) where VM : ViewModel, VM : BookmarkViewModel<*, *> {
  create(
    viewModelClass = VM::class.java,
    builder = builder,
    navController = navController,
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    content = content
  )
}

inline fun <VM> NavGraphBuilder.bookmarkComposable(
  viewModelClass: Class<VM>,
  navController: NavHostController,
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  crossinline content: @Composable VM.(NavBackStackEntry) -> Unit
) where VM : ViewModel, VM : BookmarkViewModel<*, *> {
  composable(route, arguments, deepLinks) {
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
