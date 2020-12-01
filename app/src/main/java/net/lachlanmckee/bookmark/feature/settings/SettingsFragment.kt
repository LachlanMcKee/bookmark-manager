package net.lachlanmckee.bookmark.feature.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelProviderFactory
import net.lachlanmckee.bookmark.feature.AppBottomBar
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
  @Inject
  lateinit var viewModelProviderFactory: ViewModelProviderFactory

  private val model: SettingsViewModel by viewModels { viewModelProviderFactory }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      setContent {
        MaterialTheme {
          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(text = "Settings")
                }
              )
            },
            bodyContent = {
              Text(text = "Settings screen")
            },
            bottomBar = {
              AppBottomBar(model)
            }
          )
        }
      }
    }
  }
}
