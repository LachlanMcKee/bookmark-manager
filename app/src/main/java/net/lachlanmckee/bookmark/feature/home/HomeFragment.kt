package net.lachlanmckee.bookmark.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.material.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.compose.fragmentComposeView
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

@ExperimentalLayout
@AndroidEntryPoint
class HomeFragment : Fragment() {
  @Inject
  lateinit var viewModelProviderFactory: ViewModelProviderFactory

  private val model: HomeViewModel by viewModels { viewModelProviderFactory }

  @ExperimentalCoroutinesApi
  @ExperimentalStdlibApi
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return fragmentComposeView {
      HomeScreen(model)
    }
  }
}
