package net.lachlanmckee.bookmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.integration.NodeFactory
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.routingsource.backstack.BackStack
import com.bumble.appyx.routingsource.backstack.operation.pop
import com.bumble.appyx.routingsource.backstack.operation.push
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppyxMainActivity : NodeActivity() {

  @Inject
  lateinit var composeNavigationFactories: @JvmSuppressWildcards Map<String, NodeFactory<*>>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme {
        NodeHost(integrationPoint = integrationPoint) {
          RootNode(buildContext = it) { routing, buildContext, backStack ->
            composeNavigationFactories.getValue(routing).create(buildContext)
//            when (routing) {
//              "home" -> SomeChildNode3(buildContext, backStack)
//              "other" -> SomeChildNode2(buildContext, backStack)
//              else -> SomeChildNode(buildContext, backStack)
//            }
          }
        }
      }
    }
  }
}

class RootNode(
  buildContext: BuildContext,
  private val backStack: BackStack<String> = BackStack(
    initialElement = "home",
    savedStateMap = buildContext.savedStateMap,
  ),
  private val creator: (String, BuildContext, BackStack<String>) -> Node
) : ParentNode<String>(
  routingSource = backStack,
  buildContext = buildContext
) {

  override fun resolve(routing: String, buildContext: BuildContext): Node =
    creator(routing, buildContext, backStack)

  @Composable
  override fun View(modifier: Modifier) {
    // Let's add the children to the composition
    Children(
      routingSource = backStack,
      block = {
        children<String> { child ->
          Box(Modifier.background(Color.LightGray).fillMaxSize()) {
            child()
          }
        }
      }
    )
  }
}

class SomeChildNode(
  buildContext: BuildContext,
  private val backStack: BackStack<String>
) : Node(
  buildContext = buildContext
) {
  @Composable
  override fun View(modifier: Modifier) {
    Column {
      Text("This is SomeChildNode 1")
      Controls(backStack)
    }
  }
}

class SomeChildNode2(
  buildContext: BuildContext,
  private val backStack: BackStack<String>
) : Node(
  buildContext = buildContext
) {
  @Composable
  override fun View(modifier: Modifier) {
    Column {
      Text("This is SomeChildNode 2")
      Controls(backStack)
    }
  }
}

class SomeChildNode3(
  buildContext: BuildContext,
  private val backStack: BackStack<String>
) : Node(
  buildContext = buildContext
) {
  @Composable
  override fun View(modifier: Modifier) {
    Column {
      Text("This is SomeChildNode 3")
      Controls(backStack)
    }
  }
}

@Composable
private fun Controls(backStack: BackStack<String>) {
  Row {
    TextButton(onClick = { backStack.push("home") }) {
      Text(text = "Push child 1")
    }
    TextButton(onClick = { backStack.push("other") }) {
      Text(text = "Push child 2")
    }
    TextButton(onClick = { backStack.push("bleh") }) {
      Text(text = "Push child 3")
    }
    TextButton(onClick = { backStack.pop() }) {
      Text(text = "Pop")
    }
  }
}
