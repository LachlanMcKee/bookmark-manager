package net.lachlanmckee.bookmark.feature.search.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.components.chip.layout.ChipFlowRow
import net.lachlanmckee.bookmark.components.chip.layout.ChipHorizontalList
import net.lachlanmckee.bookmark.components.row.StandardRow
import net.lachlanmckee.bookmark.feature.search.SearchViewModel
import net.lachlanmckee.bookmark.feature.search.SearchViewModel.State
import net.lachlanmckee.bookmark.feature.search.model.SearchContent
import net.lachlanmckee.bookmark.feature.search.model.TextSegment
import net.lachlanmckee.bookmark.feature.ui.BookmarkRowContent
import net.lachlanmckee.bookmark.feature.ui.RootBottomAppBar
import timber.log.Timber

@Composable
internal fun SearchScreen(
  stateFlow: StateFlow<State>,
  events: (SearchViewModel.Event) -> Unit
) {
  val state: State by stateFlow.collectAsState()
  Timber.d("SearchScreen: $state")

  BackHandler {
    events(SearchViewModel.Event.Back)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Bookmark Search")
        }
      )
    },
    content = {
      SearchContent(state, events)
    },
    bottomBar = {
      RootBottomAppBar(
        homeClick = { events(SearchViewModel.Event.HomeClicked) },
        searchClick = { events(SearchViewModel.Event.SearchClicked) },
        resetClick = { },
        settingsClick = { events(SearchViewModel.Event.SettingsClicked) }
      )
    }
  )
}

@Composable
private fun SearchContent(
  state: State,
  events: (SearchViewModel.Event) -> Unit
) {
  val lazyPagingContent: LazyPagingItems<SearchContent> =
    state.contentList.collectAsLazyPagingItems()

  Column {
    SearchTextField(state, events)

    if (state.metadata.isNotEmpty()) {
      ChipHorizontalList(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.DarkGray),
        data = state.metadata.map { it.metadata },
        isSelected = state.metadata.map { it.isSelected },
        labelFunc = { buildAnnotatedString(it.name.segments) },
        onClick = { events(SearchViewModel.Event.MetadataRowItemClicked(it)) }
      )
    }

    LazyColumn {
      items(lazyPagingContent) { content ->
        if (content != null) {
          RowContent(content, events)
        } else {
          RowContentPlaceholder()
        }
      }

      lazyPagingContent.apply {
        when {
          loadState.refresh is LoadState.Loading -> {
            item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
          }
          loadState.append is LoadState.Loading -> {
            item { LoadingItem() }
          }
          loadState.refresh is LoadState.Error -> {
            val e = lazyPagingContent.loadState.refresh as LoadState.Error
            item {
              ErrorItem(
                message = e.error.localizedMessage!!,
                modifier = Modifier.fillParentMaxSize(),
                onClickRetry = { retry() }
              )
            }
          }
          loadState.append is LoadState.Error -> {
            val e = lazyPagingContent.loadState.append as LoadState.Error
            item {
              ErrorItem(
                message = e.error.localizedMessage!!,
                onClickRetry = { retry() }
              )
            }
          }
        }
      }

      // https://proandroiddev.com/infinite-lists-with-paging-3-in-jetpack-compose-b095533aefe6
    }
  }
}

@Composable
internal fun LoadingView(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator()
  }
}

@Composable
internal fun LoadingItem() {
  CircularProgressIndicator(
    modifier = Modifier.fillMaxWidth()
      .padding(16.dp)
      .wrapContentWidth(Alignment.CenterHorizontally)
  )
}

@Composable
internal fun ErrorItem(
  message: String,
  modifier: Modifier = Modifier,
  onClickRetry: () -> Unit
) {
  Row(
    modifier = modifier.padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = message,
      maxLines = 1,
      modifier = Modifier.weight(1f),
      style = MaterialTheme.typography.h6,
      color = Color.Red
    )
    OutlinedButton(onClick = onClickRetry) {
      Text(text = "Try again")
    }
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
  state: State,
  events: (SearchViewModel.Event) -> Unit
) {
  val focusRequester = FocusRequester()
  DisposableEffect(Unit) {
    focusRequester.requestFocus()
    onDispose { }
  }

  val keyboardController = LocalSoftwareKeyboardController.current

  TextField(
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester)
      .testTag("SearchText"),
    leadingIcon = { Icon(Icons.Filled.Search, "Search") },
    trailingIcon = {
      if (state.query.isNotEmpty()) {
        Box(
          modifier = Modifier.clickable { events(SearchViewModel.Event.SearchTextChanged("")) }
        ) {
          Icon(Icons.Filled.Clear, "Clear")
        }
      }
    },
    value = state.query,
    onValueChange = {
      events(SearchViewModel.Event.SearchTextChanged(it))
    },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hideSoftwareKeyboard() }
    )
  )
}

@Composable
internal fun RowContent(
  content: SearchContent,
  events: (SearchViewModel.Event) -> Unit
) {
  when (content) {
    is SearchContent.BookmarkContent -> {
      StandardRow(
        onClick = { events(SearchViewModel.Event.ContentClicked(content)) },
        content = {
          Column {
            BookmarkRowContent(
              label = buildAnnotatedString(content.name.segments),
              link = buildAnnotatedString(content.link.segments)
            )

            ChipFlowRow(
              modifier = Modifier.padding(top = 8.dp),
              data = content.metadata,
              labelFunc = { buildAnnotatedString(it.name.segments) },
              onClick = { events(SearchViewModel.Event.MetadataRowItemClicked(it)) }
            )
          }
        }
      )
    }
  }
  Divider()
}

@Composable
internal fun RowContentPlaceholder() {
  StandardRow(
    content = {
      Column {
        BookmarkRowContent(
          label = buildAnnotatedString(listOf(TextSegment.Standard("Loading"))),
          link = buildAnnotatedString(listOf(TextSegment.Standard("Loading")))
        )
      }
    }
  )
  Divider()
}

private fun buildAnnotatedString(segments: List<TextSegment>): AnnotatedString {
  return with(AnnotatedString.Builder()) {
    segments.forEach { segment ->
      when (segment) {
        is TextSegment.Standard -> {
          append(segment.text)
        }
        is TextSegment.Highlighted -> {
          append(
            AnnotatedString
              .Builder()
              .apply {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(segment.text)
              }
              .toAnnotatedString()
          )
        }
      }
    }
    toAnnotatedString()
  }
}
