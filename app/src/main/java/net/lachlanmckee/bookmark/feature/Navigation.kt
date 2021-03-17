package net.lachlanmckee.bookmark.feature

sealed class Navigation {
  object Back : Navigation()
  object Home : Navigation()
  object Search : Navigation()
  data class Bookmark(val url: String) : Navigation()
  object Settings : Navigation()
}
