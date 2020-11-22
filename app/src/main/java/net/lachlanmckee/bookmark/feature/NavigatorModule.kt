package net.lachlanmckee.bookmark.feature

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface NavigatorModule {
  @Binds
  @ActivityScoped
  fun bindNavigator(impl: NavigatorImpl): Navigator
}
