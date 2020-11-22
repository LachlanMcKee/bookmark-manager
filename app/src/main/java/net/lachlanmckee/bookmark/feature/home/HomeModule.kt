package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.multibindings.IntoMap
import net.lachlanmckee.bookmark.di.viewmodel.ViewModelKey

@Module
@InstallIn(ActivityComponent::class)
interface HomeModule {
  @ActivityScoped
  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
