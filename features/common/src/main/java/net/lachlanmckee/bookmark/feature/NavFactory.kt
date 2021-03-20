package net.lachlanmckee.bookmark.feature

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavFactory {
  fun create(builder: NavGraphBuilder, navController: NavHostController)
}
