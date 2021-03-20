package net.lachlanmckee.bookmark.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import net.lachlanmckee.bookmark.components.chip.layout.ChipFlowRow
import net.lachlanmckee.bookmark.components.row.CheckableRow
import net.lachlanmckee.bookmark.components.row.RowText
import net.lachlanmckee.bookmark.compose.RootBottomAppBar
import net.lachlanmckee.bookmark.feature.BookmarkRowContent
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Content.BookmarkContent.Metadata

@Composable
fun HomeScreen(
  stateLiveData: LiveData<HomeViewModel.State>,
  events: (HomeViewModel.Event) -> Unit
) {
  val state: HomeViewModel.State? by stateLiveData.observeAsState()

  BackHandler {
    events(HomeViewModel.Event.Back)
  }

  if (state != null) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(text = "Bookmarks")
          }
        )
      },
      floatingActionButton = {
        HomeFab(state!!, events)
      },
      content = {
        HomeContent(state!!, events)
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
        is HomeViewModel.Content.FolderContent -> FolderRow(
          label = AnnotatedString(content.name),
          isSelected = content.selected,
          isInEditMode = state.isInEditMode,
          onClick = { events(HomeViewModel.Event.ContentClicked(content)) },
          onLongClick = { events(HomeViewModel.Event.ContentLongClicked(content)) }
        )
        is HomeViewModel.Content.BookmarkContent -> BookmarkRow(
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

@Composable
private fun BookmarkRow(
  label: AnnotatedString,
  link: AnnotatedString,
  metadata: List<Metadata>,
  isSelected: Boolean = false,
  isInEditMode: Boolean = false,
  bookmarkOnClick: () -> Unit,
  bookmarkOnLongClick: () -> Unit,
  metadataOnClick: (Long) -> Unit
) {
  CheckableRow(
    backgroundColor = Color.White,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = bookmarkOnClick,
    onLongClick = bookmarkOnLongClick,
    content = {
      CompositionLocalProvider(LocalContentColor provides Color.Black) {
        Column {
          BookmarkRowContent(
            label = label,
            link = link
          )

          ChipFlowRow(
            modifier = Modifier.padding(top = 8.dp),
            data = metadata,
            labelFunc = { AnnotatedString(it.name) },
            onClick = { metadataOnClick(it.id) }
          )
        }
      }
    }
  )
}

@Composable
private fun FolderRow(
  label: AnnotatedString,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit
) {
  CheckableRow(
    backgroundColor = Color.Gray,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      CompositionLocalProvider(LocalContentColor provides Color.White) {
        RowText(
          text = label,
          style = MaterialTheme.typography.h6
        )
      }
    }
  )
}
