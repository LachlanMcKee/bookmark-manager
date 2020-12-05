package net.lachlanmckee.bookmark.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import timber.log.Timber
import javax.inject.Inject

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
  ): View? {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
      model.backPressed()
    }

    return ComposeView(requireContext()).apply {
      setContent {
        MaterialTheme {
          model.state.observeAsState(HomeViewModel.State.Empty).value.let { state ->
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
//                                AppBottomBar(model)

                // TEMP
                BottomAppBar(cutoutShape = CircleShape) {
                  IconButton(onClick = { model.homeClicked() }) {
                    Icon(Icons.Filled.Home)
                  }
                  Spacer(Modifier.weight(1f, true))
                  IconButton(onClick = { model.resetData() }) {
                    Icon(Icons.Filled.Settings)
                  }
                }
              }
            )
          }
        }
      }
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
    when (state) {
      is HomeViewModel.State.BookmarksExist -> {
        BookmarksExistContent(state)
      }
      HomeViewModel.State.Empty -> {
        NoBookmarksContent()
      }
    }
  }

  @Composable
  private fun BookmarksExistContent(state: HomeViewModel.State.BookmarksExist) {
    LazyColumnFor(items = state.contentList) { content ->
      when (content) {
        is HomeViewModel.Content.FolderContent -> FolderRow(
          isInEditMode = state.isInEditMode,
          folderContent = content
        )
        is HomeViewModel.Content.BookmarkContent -> BookmarkRow(
          isInEditMode = state.isInEditMode,
          bookmarkContent = content
        )
      }
      Divider()
    }
  }

  @Composable
  private fun FolderRow(
    isInEditMode: Boolean,
    folderContent: HomeViewModel.Content.FolderContent
  ) {
    Surface(color = Color.Gray) {
      Row(
        Modifier
          .clickable(
            onClick = { model.contentClicked(folderContent) },
            onLongClick = { model.contentLongClicked(folderContent) }
          )
      ) {
        if (isInEditMode) {
          Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
          ) {
            Checkbox(checked = folderContent.selected, onCheckedChange = {})
          }
        }
        Box(
          Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          urlText(folderContent.name)
        }
      }
    }
  }

  @Composable
  private fun BookmarkRow(
    isInEditMode: Boolean,
    bookmarkContent: HomeViewModel.Content.BookmarkContent
  ) {
    Row(
      Modifier
        .clickable(
          onClick = { model.contentClicked(bookmarkContent) },
          onLongClick = { model.contentLongClicked(bookmarkContent) }
        )
    ) {
      if (isInEditMode) {
        Box(
          modifier = Modifier.fillMaxHeight(),
          contentAlignment = Alignment.Center
        ) {
          Checkbox(checked = bookmarkContent.selected, onCheckedChange = {})
        }
      }
      Box(
        Modifier
          .weight(1f)
          .padding(16.dp)
      ) {
        urlText(bookmarkContent.name)
      }
      Box(
        Modifier
          .weight(2f)
          .padding(16.dp)
      ) {
        urlText(bookmarkContent.link)
      }
    }
  }

  @Composable
  private fun NoBookmarksContent() {
  }

  @Composable
  private fun urlText(text: String) {
    Text(
      text = text,
      textAlign = TextAlign.Left,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1
    )
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }
}
