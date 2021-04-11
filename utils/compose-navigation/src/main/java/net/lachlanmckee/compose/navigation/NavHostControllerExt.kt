package net.lachlanmckee.compose.navigation

import androidx.navigation.NavHostController

fun NavHostController.isCurrentRoute(route: String): Boolean {
  return currentBackStackEntry?.destination?.id == ComposeRouteUtil.createNavigationRouteId(route)
}
