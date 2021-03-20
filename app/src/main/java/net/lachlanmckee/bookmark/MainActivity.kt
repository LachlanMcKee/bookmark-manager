package net.lachlanmckee.bookmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.bookmark.feature.NavFactory
import net.lachlanmckee.bookmark.feature.search.SearchScreen
import net.lachlanmckee.bookmark.feature.search.SearchViewModelImpl
import javax.inject.Inject

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
    }
  }
}
