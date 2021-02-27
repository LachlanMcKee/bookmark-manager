package net.lachlanmckee.bookmark.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.fragmentComposeView
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

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
    return fragmentComposeView { SearchScreen(model) }
  }
}
