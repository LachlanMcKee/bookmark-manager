package net.lachlanmckee.bookmark.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumble.appyx.core.integration.NodeFactory
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import net.lachlanmckee.bookmark.feature.home.ui.HomeScreen
import javax.inject.Singleton

internal class HomeNodeFactory : NodeFactory<HomeNode> {
  override fun create(buildContext: BuildContext): HomeNode =
    HomeNode(buildContext)
}

internal class HomeNode(
  buildContext: BuildContext,
//  private val backStack: BackStack<String>
) : Node(
  buildContext = buildContext
) {
  @Composable
  override fun View(modifier: Modifier) {
    val viewModel = viewModel<HomeViewModelImpl>()
    HomeScreen(viewModel.state.collectAsState().value, viewModel.eventConsumer)
  }
}

@Module
@InstallIn(SingletonComponent::class)
internal class ComposeNavigationFactoryModule {
  @Singleton
  @Provides
  @IntoMap
  @StringKey("home")
  fun provideHomeNodeFactory(): NodeFactory<*> =
    HomeNodeFactory()
}
