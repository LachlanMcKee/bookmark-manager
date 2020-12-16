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
  private fun SearchContent(state: SearchViewModel.State) {
    Timber.d("state: $state")
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
      when (state) {
        is SearchViewModel.State.Empty -> {
        }
        is SearchViewModel.State.Results -> {
          items(state.contentList) { RowContent(it) }
        }
      }
    }
  }

  @Composable
  private fun SearchTextField(state: SearchViewModel.State) {
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
        ClickableRow(
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
