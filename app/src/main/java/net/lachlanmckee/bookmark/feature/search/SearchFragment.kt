package net.lachlanmckee.bookmark.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.fragmentComposeView
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import net.lachlanmckee.bookmark.feature.BookmarkRow
import net.lachlanmckee.bookmark.feature.RootBottomAppBar
import timber.log.Timber
import javax.inject.Inject

@ExperimentalFocus
@AndroidEntryPoint
class SearchFragment : Fragment() {
  @Inject
  lateinit var viewModelProviderFactory: ViewModelProviderFactory

  private val model: SearchViewModel by viewModels { viewModelProviderFactory }

  @ExperimentalCoroutinesApi
  @ExperimentalStdlibApi
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
      model.backPressed()
    }

    return fragmentComposeView(model.state) { state ->
      Scaffold(
        topBar = {
          TopAppBar(
            title = {
              Text(text = "Bookmark Search")
            }
          )
        },
        bodyContent = {
          SearchContent(state)
        },
        bottomBar = {
          RootBottomAppBar(
            homeClick = { model.homeClicked() },
            searchClick = { model.searchClicked() },
            resetClick = { },
            settingsClick = { model.settingsClicked() }
          )
        }
      )
    }
  }

  @Composable
  private fun SearchContent(state: SearchViewModel.State) {
    Timber.d("state: $state")
    Column {
      SearchTextField(state)
      when (state) {
        SearchViewModel.State.Empty -> {
        }
        is SearchViewModel.State.Results -> {
          ResultsContent(state)
        }
      }
    }
  }

  @Composable
  private fun SearchTextField(state: SearchViewModel.State) {
    val focusRequester = FocusRequester()
    onActive(callback = { focusRequester.requestFocus() })
    TextField(
      modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester),
      leadingIcon = { Icon(Icons.Filled.Search) },
      trailingIcon = {
        if (state.query.isNotEmpty()) {
          Box(
            modifier = Modifier.clickable { model.searchTextChanged("") }
          ) {
            Icon(Icons.Filled.Clear)
          }
        }
      },
      value = state.query,
      onValueChange = {
        Timber.d("Search changed: $it")
        model.searchTextChanged(it)
      }
    )
  }

  @Composable
  private fun ResultsContent(state: SearchViewModel.State.Results) {
    LazyColumnFor(items = state.contentList) { content ->
      when (content) {
        is SearchViewModel.Content.BookmarkContent -> BookmarkRow(
          label = content.name,
          link = content.link,
          isSelected = false,
          isInEditMode = false,
          onClick = { model.contentClicked(content) },
          onLongClick = { }
        )
      }
      Divider()
    }
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }
}
