package net.lachlanmckee.bookmark.feature

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.scopes.FragmentScoped
import net.lachlanmckee.bookmark.R
import javax.inject.Inject

interface Navigator {
  fun openBookmark(bookmarkUrl: String)
  fun home()
  fun search()
  fun settings()
  fun back()
}

class NavigatorImpl @Inject constructor(
  @FragmentScoped private val fragment: Fragment
) : Navigator {

  private val navController: NavController
    get() = fragment.findNavController()

  override fun openBookmark(bookmarkUrl: String) {
    fragment.requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkUrl)))
  }

  override fun home() {
    navController.popBackStack(R.id.home_dest, false)
  }

  override fun search() {
    if (navController.currentDestination?.id != R.id.search_dest) {
      navController.navigate(R.id.search_dest)
    }
  }

  override fun settings() {
    if (navController.currentDestination?.id != R.id.settings_dest) {
      navController.navigate(R.id.settings_dest)
    }
  }

  override fun back() {
    if (!navController.popBackStack()) {
      fragment.requireActivity().finish()
    }
  }
}
