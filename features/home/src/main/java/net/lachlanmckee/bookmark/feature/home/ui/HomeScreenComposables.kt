package net.lachlanmckee.bookmark.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.LiveData
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import net.lachlanmckee.bookmark.feature.ui.RootBottomAppBar

@Composable
internal fun HomeScreen(
  stateLiveData: LiveData<HomeViewModel.State>,
  events: (HomeViewModel.Event) -> Unit
) {
  val state: HomeViewModel.State by stateLiveData.observeAsState(HomeViewModel.State.Empty)

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

@Composable
private fun HomeFab(
  state: HomeViewModel.State,
  events: (HomeViewModel.Event) -> Unit
) {
  when (state) {
    HomeViewModel.State.Empty -> {
    }
    is HomeViewModel.State.BookmarksExist -> {
      if (state.isInEditMode) {
        FloatingActionButton(onClick = { events(HomeViewModel.Event.Delete) }) {
          Icon(Icons.Filled.Delete, "Delete")
        }
      }
    }
  }
}

@Composable
private fun HomeContent(
  state: HomeViewModel.State,
  events: (HomeViewModel.Event) -> Unit
) {
  when (state) {
    HomeViewModel.State.Empty -> {
    }
    is HomeViewModel.State.BookmarksExist -> {
      BookmarksExistContent(state, events)
    }
  }
}

@Composable
private fun BookmarksExistContent(
  state: HomeViewModel.State.BookmarksExist,
  events: (HomeViewModel.Event) -> Unit
) {
  LazyColumn {
    items(state.contentList) { content ->
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
      Divider()
    }
  }
}
