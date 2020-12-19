package net.lachlanmckee.bookmark.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.*
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import net.lachlanmckee.bookmark.feature.BookmarkRowContent
import timber.log.Timber
import javax.inject.Inject

@ExperimentalLayout
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
  private fun SearchContent(state: SearchViewModel.Results) {
    Timber.d("state: $state")

    val lazyPagingContent = state.contentList.collectAsLazyPagingItems()

    LazyColumn(
      state = rememberLazyListState(),
      horizontalAlignment = Alignment.Start
    ) {
      item {
        SearchTextField(state)
      }
      if (state.metadata.isNotEmpty()) {
        item {
          ChipCollection(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.DarkGray)
              .padding(8.dp),
            data = state.metadata,
            labelFunc = { buildAnnotatedString(it.name.segments) },
            onClick = model::metadataRowItemClicked
          )
        }
      }
      if (state.selectedMetadata.isNotEmpty()) {
        item {
          ChipCollection(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.Cyan)
              .padding(8.dp),
            data = state.selectedMetadata,
            showCloseIcon = true,
            labelFunc = { buildAnnotatedString(it.name.segments) },
            onClick = model::metadataFilterClicked
          )
        }
      }

      items(lazyPagingContent) {
        if (it != null) {
          RowContent(it)
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
  private fun SearchTextField(state: SearchViewModel.Results) {
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
  fun RowContent(content: SearchViewModel.Content) {
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

              ChipCollection(
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
            append(AnnotatedString
              .Builder()
              .apply {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(segment.text)
              }
              .toAnnotatedString())
          }
        }
      }
      toAnnotatedString()
    }
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }
}
