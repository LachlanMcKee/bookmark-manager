package net.lachlanmckee.bookmark.feature.home

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
interface HomeModule {
  @FragmentScoped
  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
