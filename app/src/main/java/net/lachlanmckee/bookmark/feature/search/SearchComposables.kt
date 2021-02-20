package net.lachlanmckee.bookmark.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.ChipFlowRow
import net.lachlanmckee.bookmark.compose.ChipHorizontalList
import net.lachlanmckee.bookmark.compose.RootBottomAppBar
import net.lachlanmckee.bookmark.compose.StandardRow
import net.lachlanmckee.bookmark.feature.BookmarkRowContent

@ExperimentalLayout
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Composable
fun SearchScreen(model: SearchViewModel) {
  val state: SearchViewModel.Results? by model.state.observeAsState()

  BackHandler {
    model.backPressed()
  }

  if (state != null) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(text = "Bookmark Search")
          }
        )
      },
      bodyContent = {
        SearchContent(model, state!!)
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

@ExperimentalLayout
@Composable
private fun SearchContent(model: SearchViewModel, state: SearchViewModel.Results) {
  val lazyPagingContent: LazyPagingItems<SearchViewModel.Content> = state.contentList.collectAsLazyPagingItems()

  Column {
    SearchTextField(model, state)

    if (state.metadata.isNotEmpty()) {
      ChipHorizontalList(
        modifier = Modifier.background(Color.DarkGray),
        data = state.metadata.map { it.metadata },
        isSelected = state.metadata.map { it.isSelected },
        labelFunc = { buildAnnotatedString(it.name.segments) },
        onClick = { model.metadataRowItemClicked(it) }
      )
    }

    LazyColumn {
      items(lazyPagingContent) { content ->
        if (content != null) {
          RowContent(model, content)
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
fun LoadingView(
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
fun LoadingItem() {
  CircularProgressIndicator(
    modifier = Modifier.fillMaxWidth()
      .padding(16.dp)
      .wrapContentWidth(Alignment.CenterHorizontally)
  )
}

@Composable
fun ErrorItem(
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

@Composable
private fun SearchTextField(model: SearchViewModel, state: SearchViewModel.Results) {
  val focusRequester = FocusRequester()
  DisposableEffect(Unit) {
    focusRequester.requestFocus()
    onDispose { }
  }
  TextField(
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester),
    leadingIcon = { Icon(Icons.Filled.Search, "Search") },
    trailingIcon = {
      if (state.query.isNotEmpty()) {
        Box(
          modifier = Modifier.clickable { model.searchTextChanged("") }
        ) {
          Icon(Icons.Filled.Clear, "Clear")
        }
      }
    },
    value = state.query,
    onValueChange = {
      model.searchTextChanged(it)
    }
  )
}

@ExperimentalLayout
@Composable
fun RowContent(model: SearchViewModel, content: SearchViewModel.Content) {
  when (content) {
    is SearchViewModel.Content.BookmarkContent -> {
      StandardRow(
        onClick = { model.contentClicked(content) },
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
              onClick = model::metadataRowItemClicked
            )
          }
        }
      )
    }
  }
  Divider()
}

@Composable
fun RowContentPlaceholder() {
  StandardRow(
    content = {
      Column {
        BookmarkRowContent(
          label = buildAnnotatedString(listOf(SearchViewModel.TextSegment.Standard("Loading"))),
          link = buildAnnotatedString(listOf(SearchViewModel.TextSegment.Standard("Loading")))
        )
      }
    }
  )
  Divider()
}

private fun buildAnnotatedString(segments: List<SearchViewModel.TextSegment>): AnnotatedString {
  return with(AnnotatedString.Builder()) {
    segments.forEach { segment ->
      when (segment) {
        is SearchViewModel.TextSegment.Standard -> {
          append(segment.text)
        }
        is SearchViewModel.TextSegment.Highlighted -> {
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
