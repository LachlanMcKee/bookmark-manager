package net.lachlanmckee.bookmark.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.components.list.ScrollToTopLazyColumn
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import net.lachlanmckee.bookmark.feature.ui.RootBottomAppBar
import timber.log.Timber

@Composable
internal fun HomeScreen(
  stateFlow: StateFlow<HomeViewModel.State>,
  events: (HomeViewModel.Event) -> Unit
) {
  val state: HomeViewModel.State by stateFlow.collectAsState()
  Timber.d("HomeScreen $state")

  BackHandler {
    events(HomeViewModel.Event.Back)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Bookmarks")
        }
      )
    },
    floatingActionButton = {
      HomeFab(state, events)
    },
    content = {
      HomeContent(state, events)
    },
    bottomBar = {
      RootBottomAppBar(
        homeClick = { events(HomeViewModel.Event.HomeClicked) },
        searchClick = { events(HomeViewModel.Event.SearchClicked) },
        resetClick = { events(HomeViewModel.Event.ResetDataClicked) },
        settingsClick = { events(HomeViewModel.Event.SettingsClicked) }
      )
    }
  )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeFab(
  state: HomeViewModel.State,
  events: (HomeViewModel.Event) -> Unit
) {
  AnimatedVisibility(
    visible = state is HomeViewModel.State.BookmarksExist && state.isInEditMode,
    enter = fadeIn(),
    exit = fadeOut(),
    content = {
      FloatingActionButton(onClick = { events(HomeViewModel.Event.Delete) }) {
        Icon(Icons.Filled.Delete, "Delete")
      }
    }
  )
}

@Composable
private fun HomeContent(
  state: HomeViewModel.State,
  events: (HomeViewModel.Event) -> Unit
) {
  if (state is HomeViewModel.State.BookmarksExist) {
    BookmarksExistContent(state, events)
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BookmarksExistContent(
  state: HomeViewModel.State.BookmarksExist,
  events: (HomeViewModel.Event) -> Unit
) {
  ScrollToTopLazyColumn {
    items(
      items = state.contentList,
      key = { content -> content.javaClass.simpleName + content.id }
    ) { content ->
      BookmarkRow(state, content, events)
      Divider()
    }
  }
}

@Composable
private fun BookmarkRow(
  state: HomeViewModel.State.BookmarksExist,
  content: HomeContent,
  events: (HomeViewModel.Event) -> Unit
) {
  when (content) {
    is HomeContent.FolderContent -> FolderRow(
      label = AnnotatedString(content.name),
      isSelected = content.selected,
      isInEditMode = state.isInEditMode,
      onClick = { events(HomeViewModel.Event.ContentClicked(content)) },
      onLongClick = { events(HomeViewModel.Event.ContentLongClicked(content)) }
    )
    is HomeContent.BookmarkContent -> BookmarkRow(
      label = AnnotatedString(content.name),
      link = AnnotatedString(content.link),
      metadata = content.metadata,
      isSelected = content.selected,
      isInEditMode = state.isInEditMode,
      bookmarkOnClick = { events(HomeViewModel.Event.ContentClicked(content)) },
      bookmarkOnLongClick = { events(HomeViewModel.Event.ContentLongClicked(content)) },
      metadataOnClick = { }
    )
  }
}
