package net.lachlanmckee.bookmark.feature.form

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import net.lachlanmckee.bookmark.feature.StandardViewModel
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.Event
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.State
import net.lachlanmckee.bookmark.feature.model.Navigation
import javax.inject.Inject

@HiltViewModel
internal class BookmarkFormViewModelImpl @Inject constructor() : StandardViewModel<State, Event>(), BookmarkFormViewModel {

  override val initialState: State = State.Empty

  override fun createState(): Flow<State> = emptyFlow()

  override val eventConsumer: (Event) -> Unit = { event ->
    when (event) {
      is Event.Back -> navigate(Navigation.Back)
      is Event.Save -> Unit // TODO
    }
  }
}
