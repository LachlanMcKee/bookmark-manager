package net.lachlanmckee.bookmark.feature.form.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel
import timber.log.Timber

@Composable
internal fun BookmarkFormScreen(
  stateFlow: StateFlow<BookmarkFormViewModel.State>,
  events: (BookmarkFormViewModel.Event) -> Unit
) {
  val state: BookmarkFormViewModel.State by stateFlow.collectAsState()
  Timber.d("BookmarkFormScreen $state")

  BackHandler {
    events(BookmarkFormViewModel.Event.Back)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Add Bookmark")
        },
        navigationIcon = {
          IconButton(onClick = { events(BookmarkFormViewModel.Event.Back) }) {
            Icon(Icons.Filled.ArrowBack, "Back")
          }
        }
      )
    },
    floatingActionButton = {
      FloatingActionButton(onClick = { events(BookmarkFormViewModel.Event.Save) }) {
        Icon(Icons.Filled.Add, "Add")
      }
    },
    content = {
      Content()
    }
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Content() {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var nameText by remember { mutableStateOf(TextFieldValue()) }
    TextField(
      value = nameText,
      onValueChange = {
        nameText = it
      },
      label = { Text("Name") },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
      keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
      )
    )

    var emailText by remember { mutableStateOf(TextFieldValue()) }
    TextField(
      value = emailText,
      onValueChange = {
        emailText = it
      },
      label = { Text("Email") },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = { keyboardController?.hide() }
      )
    )
  }
}
