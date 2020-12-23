package net.lachlanmckee.bookmark.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.RootBottomAppBar
import net.lachlanmckee.bookmark.compose.fragmentComposeView
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import timber.log.Timber
import javax.inject.Inject

@ExperimentalLayout
@AndroidEntryPoint
class HomeFragment : Fragment() {
  @Inject
  lateinit var viewModelProviderFactory: ViewModelProviderFactory

  private val model: HomeViewModel by viewModels { viewModelProviderFactory }

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
              Text(text = "Bookmarks")
            }
          )
        },
        floatingActionButton = {
          HomeFab(state)
        },
        bodyContent = {
          HomeContent(state)
        },
        bottomBar = {
          RootBottomAppBar(
            homeClick = { model.homeClicked() },
            searchClick = { model.searchClicked() },
            resetClick = { model.resetData() },
            settingsClick = { model.settingsClicked() }
          )
        }
      )
    }
  }

  @Composable
  private fun HomeFab(state: HomeViewModel.State) {
    when (state) {
      HomeViewModel.State.Empty -> {
      }
      is HomeViewModel.State.BookmarksExist -> {
        if (state.isInEditMode) {
          FloatingActionButton(onClick = { model.deleteClicked() }) {
            Icon(Icons.Filled.Delete)
          }
        }
      }
    }
  }

  @Composable
  private fun HomeContent(state: HomeViewModel.State) {
    Timber.d("HomeContent. state: $state")
    when (state) {
      HomeViewModel.State.Empty -> {
      }
      is HomeViewModel.State.BookmarksExist -> {
        BookmarksExistContent(state)
      }
    }
  }

  @Composable
  private fun BookmarksExistContent(state: HomeViewModel.State.BookmarksExist) {
    LazyColumn {
      items(state.contentList) { content ->
        when (content) {
          is HomeViewModel.Content.FolderContent -> FolderRow(
            label = AnnotatedString(content.name),
            isSelected = content.selected,
            isInEditMode = state.isInEditMode,
            onClick = { model.contentClicked(content) },
            onLongClick = { model.contentLongClicked(content) }
          )
          is HomeViewModel.Content.BookmarkContent -> BookmarkRow(
            label = AnnotatedString(content.name),
            link = AnnotatedString(content.link),
            metadata = content.metadata,
            isSelected = content.selected,
            isInEditMode = state.isInEditMode,
            bookmarkOnClick = { model.contentClicked(content) },
            bookmarkOnLongClick = { model.contentLongClicked(content) },
            metadataOnClick = {  }
          )
        }
        Divider()
      }
    }
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }
}
