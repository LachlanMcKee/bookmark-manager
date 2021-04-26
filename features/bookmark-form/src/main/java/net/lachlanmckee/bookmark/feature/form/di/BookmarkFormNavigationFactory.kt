package net.lachlanmckee.bookmark.feature.form.di

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModelImpl
import net.lachlanmckee.bookmark.feature.form.ui.BookmarkFormScreen
import net.lachlanmckee.hilt.compose.navigation.factory.ComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.HiltComposeNavigationFactory
import javax.inject.Inject

@HiltComposeNavigationFactory
internal class BookmarkFormNavigationFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : ComposeNavigationFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<BookmarkFormViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "bookmark-form",
      content = {
        BookmarkFormScreen(
          stateFlow = state,
          events = eventConsumer
        )
      }
    )
  }
}
