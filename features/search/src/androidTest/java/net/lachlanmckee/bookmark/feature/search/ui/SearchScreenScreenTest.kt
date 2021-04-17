package net.lachlanmckee.bookmark.feature.search.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import net.lachlanmckee.bookmark.feature.search.SearchViewModel
import net.lachlanmckee.bookmark.feature.search.model.*
import org.junit.Rule
import org.junit.Test

class SearchScreenScreenTest : ScreenshotTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun verifySearchScreenDesign() {
    val state: MutableStateFlow<SearchViewModel.State> =
      MutableStateFlow(SearchViewModel.State.emptyState)

    composeRule.setContent {
      MaterialTheme {
        SearchScreen(state) {}
      }
    }

    compareScreenshot(composeRule, "Empty")

    state.value =
      SearchViewModel.State(
        query = "Query",
        metadata = emptyList(),
        contentList = emptyFlow()
      )

    compareScreenshot(composeRule, "No_Results")

    state.value =
      SearchViewModel.State(
        query = "Query",
        metadata = listOf(
          SelectableMetadata(
            isSelected = true,
            metadata = SearchMetadata(
              id = 1,
              name = createStandardSearchText("Metadata 1")
            )
          ),
          SelectableMetadata(
            isSelected = false,
            metadata = SearchMetadata(
              id = 2,
              name = createStandardSearchText("Metadata 2")
            )
          )
        ),
        contentList = flowOf(
          PagingData.from(
            listOf(
              SearchContent.BookmarkContent(
                id = 1,
                name = SearchText(
                  fullText = "Query matched",
                  segments = listOf(
                    TextSegment.Highlighted("Query"),
                    TextSegment.Standard(" matched")
                  )
                ),
                link = SearchText(
                  fullText = "https://www.query-website.com/",
                  segments = listOf(
                    TextSegment.Standard("https://www."),
                    TextSegment.Highlighted("query"),
                    TextSegment.Standard("-website.com/")
                  )
                ),
                metadata = listOf(
                  SearchMetadata(
                    id = 1,
                    name = SearchText(
                      fullText = "Metadata 1",
                      segments = listOf(
                        TextSegment.Highlighted("Metadata 1")
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )

    compareScreenshot(composeRule, "With_Results")
  }

  private fun createStandardSearchText(text: String): SearchText {
    return SearchText(
      fullText = text,
      segments = listOf(TextSegment.Standard(text))
    )
  }
}
