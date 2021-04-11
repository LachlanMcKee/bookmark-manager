package net.lachlanmckee.compose.navigation

object ComposeRouteUtil {
  fun createNavigationRouteId(route: String): Int {
    return "android-app://androidx.navigation.compose/$route".hashCode()
  }
}
