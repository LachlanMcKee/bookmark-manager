package net.lachlanmckee.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable

inline fun <VM> NavGraphBuilder.viewModelComposable(
  viewModelClass: Class<VM>,
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  crossinline content: @Composable VM.(NavBackStackEntry) -> Unit
) where VM : ViewModel {

  composable(route, arguments, deepLinks) {
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

    content(viewModel, it)
  }
}

inline fun <reified VM> NavGraphBuilder.viewModelComposable(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  crossinline content: @Composable VM.(NavBackStackEntry) -> Unit
) where VM : ViewModel {

  viewModelComposable(
    viewModelClass = VM::class.java,
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    content = content
  )
}
