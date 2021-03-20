package net.lachlanmckee.bookmark.feature

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NamedNavArgument

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
