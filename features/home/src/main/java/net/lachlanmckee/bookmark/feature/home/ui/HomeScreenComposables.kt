package net.lachlanmckee.bookmark.feature.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.components.list.ScrollToTopLazyColumn
import net.lachlanmckee.bookmark.compose.ConditionalComposable
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
          Text(text = state.folderName ?: "Bookmarks")
        },
        navigationIcon = ConditionalComposable(!state.isRootFolder) {
          IconButton(onClick = { events(HomeViewModel.Event.Back) }) {
            Icon(Icons.Filled.ArrowBack, "Back")
          }
        }
      )
    },
    floatingActionButton = {
      HomeFab(state, events)
    },
    content = { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding)) {
        HomeContent(
          state = state,
          onContentClicked = { events(HomeViewModel.Event.ContentClicked(it)) },
          onContentLongClicked = { events(HomeViewModel.Event.ContentLongClicked(it)) }
        )
      }
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
  if (state is HomeViewModel.State.BookmarksExist && state.isInEditMode) {
    FloatingActionButton(onClick = { events(HomeViewModel.Event.Delete) }) {
      Icon(Icons.Filled.Delete, "Delete")
    }
  } else {
    FloatingActionButton(onClick = { events(HomeViewModel.Event.Add) }) {
      Icon(Icons.Filled.Add, "Add")
    }
  }
}

@Composable
private fun HomeContent(
  state: HomeViewModel.State,
  onContentClicked: (HomeContent) -> Unit,
  onContentLongClicked: (HomeContent) -> Unit
) {
  when (state) {
    is HomeViewModel.State.BookmarksExist -> {
      BookmarksExistContent(
        contentList = state.contentList,
        isInEditMode = state.isInEditMode,
        onContentClicked = onContentClicked,
        onContentLongClicked = onContentLongClicked
      )
    }
    is HomeViewModel.State.NoBookmarks -> {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.body1,
          text = "Folder is empty"
        )
      }
    }
    is HomeViewModel.State.Empty -> {
    }
  }
}

@Composable
private fun BookmarksExistContent(
  contentList: List<HomeContent>,
  isInEditMode: Boolean,
  onContentClicked: (HomeContent) -> Unit,
  onContentLongClicked: (HomeContent) -> Unit
) {
  ScrollToTopLazyColumn {
    items(
      items = contentList,
      key = { content -> content.javaClass.simpleName + content.id }
    ) { content ->
      BookmarkRow(
        content = content,
        isInEditMode = isInEditMode,
        onContentClicked = onContentClicked,
        onContentLongClicked = onContentLongClicked
      )
      Divider()
    }
  }
}

@Composable
private fun BookmarkRow(
  content: HomeContent,
  isInEditMode: Boolean,
  onContentClicked: (HomeContent) -> Unit,
  onContentLongClicked: (HomeContent) -> Unit
) {
  when (content) {
    is HomeContent.FolderContent -> FolderRow(
      label = AnnotatedString(content.name),
      isSelected = content.selected,
      isInEditMode = isInEditMode,
      onClick = { onContentClicked(content) },
      onLongClick = { onContentLongClicked(content) }
    )
    is HomeContent.BookmarkContent -> BookmarkRow(
      label = AnnotatedString(content.name),
      link = AnnotatedString(content.link),
      metadata = content.metadata,
      isSelected = content.selected,
      isInEditMode = isInEditMode,
      bookmarkOnClick = { onContentClicked(content) },
      bookmarkOnLongClick = { onContentLongClicked(content) },
      metadataOnClick = { }
    )
  }
}
