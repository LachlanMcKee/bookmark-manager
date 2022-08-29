package net.lachlanmckee.bookmark.feature.form

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.StandardViewModel
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.Event
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.State
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

@HiltViewModel
internal class BookmarkFormViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository
) : StandardViewModel<State, Event>(),
  BookmarkFormViewModel {

  private val currentStateFlowable: MutableStateFlow<State> = MutableStateFlow(State.emptyState)

  override val initialState: State = State.emptyState

  override fun createState(): Flow<State> = currentStateFlowable

  override val eventConsumer: (Event) -> Unit = { event ->
    when (event) {
      is Event.Back -> navigate(Navigation.Back)
      is Event.Save -> save()
      is Event.NameUpdated ->
        currentStateFlowable.value = currentStateFlowable.value.copy(name = event.name)
      is Event.UrlUpdated ->
        currentStateFlowable.value = currentStateFlowable.value.copy(url = event.url)
    }
  }

  private fun save() {
    val currentState = currentStateFlowable.value
    if (currentState.name.isBlank() || currentState.url.isBlank()) {
      return
    }

    currentStateFlowable.value = currentState.copy(loading = true)
    viewModelScope.launch {
      bookmarkRepository.saveBookmark(
        name = currentState.name,
        url = currentState.url
      )
      currentStateFlowable.value = currentState.copy(loading = false)
      navigate(Navigation.Back)
    }
  }
}
