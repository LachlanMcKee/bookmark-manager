package net.lachlanmckee.bookmark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.Navigation
import net.lachlanmckee.bookmark.feature.home.HomeScreen
import net.lachlanmckee.bookmark.feature.home.HomeViewModelImpl
import net.lachlanmckee.bookmark.feature.search.SearchScreen
import net.lachlanmckee.bookmark.feature.search.SearchViewModelImpl
import net.lachlanmckee.bookmark.feature.settings.SettingsScreen
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var navFactories: @JvmSuppressWildcards Set<NavFactory>


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BookmarkApp()
    }
  }

  @Composable
  fun BookmarkApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {

      navFactories.forEach { factory ->
        factory.create(this, navController)
      }

      bookmarkComposable<SearchViewModelImpl>(
        viewModelClass = SearchViewModelImpl::class.java,
        navController = navController,
        route = "search",
        content = {
          SearchScreen(
            stateLiveData = state,
            events = eventConsumer
          )
        }
      )
      bookmarkComposable<SettingsViewModel>(
        viewModelClass = SettingsViewModel::class.java,
        navController = navController,
        route = "settings",
        content = {
          SettingsScreen(this)
        }
      )
    }
  }
}
