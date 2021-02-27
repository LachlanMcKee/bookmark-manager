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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.CheckableRow
import net.lachlanmckee.bookmark.compose.ChipFlowRow
import net.lachlanmckee.bookmark.compose.RootBottomAppBar
import net.lachlanmckee.bookmark.compose.RowText
import net.lachlanmckee.bookmark.feature.BookmarkRowContent
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Content.BookmarkContent.Metadata

@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Composable
fun HomeScreen(model: HomeViewModel) {
  val state: HomeViewModel.State? by model.state.observeAsState()

  BackHandler {
    model.backPressed()
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
        HomeFab(model, state!!)
      },
      content = {
        HomeContent(model, state!!)
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
private fun HomeFab(model: HomeViewModel, state: HomeViewModel.State) {
  when (state) {
    HomeViewModel.State.Empty -> {
    }
    is HomeViewModel.State.BookmarksExist -> {
      if (state.isInEditMode) {
        FloatingActionButton(onClick = { model.deleteClicked() }) {
          Icon(Icons.Filled.Delete, "Delete")
        }
      }
    }
  }
}

@Composable
private fun HomeContent(model: HomeViewModel, state: HomeViewModel.State) {
  when (state) {
    HomeViewModel.State.Empty -> {
    }
    is HomeViewModel.State.BookmarksExist -> {
      BookmarksExistContent(model, state)
    }
  }
}

@Composable
private fun BookmarksExistContent(model: HomeViewModel, state: HomeViewModel.State.BookmarksExist) {
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
          metadataOnClick = { }
        )
      }
      Divider()
    }
  }
}

@Composable
fun BookmarkRow(
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
fun FolderRow(
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
