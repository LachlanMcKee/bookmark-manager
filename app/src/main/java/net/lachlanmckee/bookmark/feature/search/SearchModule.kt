package net.lachlanmckee.bookmark.feature.search

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.multibindings.IntoMap
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelKey

@Module
@InstallIn(FragmentComponent::class)
interface SearchModule {
  @FragmentScoped
  @Binds
  @IntoMap
  @ViewModelKey(SearchViewModel::class)
  fun bindHomeViewModel(viewModel: SearchViewModel): ViewModel
}
