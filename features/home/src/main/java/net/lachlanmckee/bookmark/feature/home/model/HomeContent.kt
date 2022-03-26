package net.lachlanmckee.bookmark.feature.home.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class HomeContent {
  abstract val id: Long
  abstract val name: String
  abstract val selected: Boolean
  abstract val contentType: String

  @Immutable
  data class FolderContent(
    override val id: Long,
    override val name: String,
    override val selected: Boolean
  ) : HomeContent() {
    override val contentType: String = "bookmark"
  }

  @Immutable
  data class BookmarkContent(
    override val id: Long,
    override val name: String,
    override val selected: Boolean,
    val link: String,
    val metadata: List<Metadata>
  ) : HomeContent() {
    override val contentType: String = "folder"

    @Immutable
    data class Metadata(
      val id: Long,
      val name: String
    )
  }
}
