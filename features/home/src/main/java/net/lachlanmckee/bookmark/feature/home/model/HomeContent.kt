package net.lachlanmckee.bookmark.feature.home.model

internal sealed class HomeContent {
  abstract val id: Long
  abstract val name: String
  abstract val selected: Boolean

  data class FolderContent(
    override val id: Long,
    override val name: String,
    override val selected: Boolean
  ) : HomeContent()

  data class BookmarkContent(
    override val id: Long,
    override val name: String,
    override val selected: Boolean,
    val link: String,
    val metadata: List<Metadata>
  ) : HomeContent() {
    data class Metadata(
      val id: Long,
      val name: String
    )
  }
}
