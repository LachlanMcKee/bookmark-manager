package net.lachlanmckee.bookmark.feature

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
interface NavigatorModule {
  @Binds
  @FragmentScoped
  fun bindNavigator(impl: NavigatorImpl): Navigator
}
