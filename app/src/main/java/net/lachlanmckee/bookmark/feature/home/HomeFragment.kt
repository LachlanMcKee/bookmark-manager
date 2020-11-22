package net.lachlanmckee.bookmark.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private val model: HomeViewModel by viewModels { viewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(onClick = { model.addBookmark() }) {
                                Icon(Icons.Filled.Add)
                            }
                        },
                        bodyContent = {
                            screen()
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun screen() {
        model.state.observeAsState(HomeViewModel.State.Empty).value.let { state ->
            when (state) {
                is HomeViewModel.State.BookmarksExist -> {
                    bookmarksExist(state)
                }
                HomeViewModel.State.Empty -> {
                    noBookmarks()
                }
            }
        }
    }

    @Composable
    private fun bookmarksExist(state: HomeViewModel.State.BookmarksExist) {
        LazyColumnFor(items = state.bookmarks) { bookmark ->
            Row(
                Modifier
                    .clickable(onClick = { model.bookmarkClicked(bookmark) })
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    urlText(bookmark.name)
                }
                Box(
                    Modifier
                        .weight(2f)
                        .padding(16.dp)
                ) {
                    urlText(bookmark.link)
                }
            }
            Divider()
        }
    }

    @Composable
    private fun noBookmarks() {
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
