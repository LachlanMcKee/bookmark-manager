package net.lachlanmckee.compose.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder

fun NavOptionsBuilder.popUpToRoute(route: String, popUpToBuilder: PopUpToBuilder.() -> Unit = {}) {
  popUpTo(
    id = ComposeRouteUtil.createNavigationRouteId(route),
    popUpToBuilder = popUpToBuilder
  )
}
