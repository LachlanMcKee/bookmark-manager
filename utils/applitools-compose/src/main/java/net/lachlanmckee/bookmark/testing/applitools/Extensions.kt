package net.lachlanmckee.bookmark.testing.applitools

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.applitools.eyes.android.common.BatchInfo
import com.applitools.eyes.android.components.androidx.AndroidXComponentsProvider
import com.applitools.eyes.android.espresso.Eyes
import net.lachlanmckee.bookmark.util.applitools.BuildConfig

@RequiresApi(Build.VERSION_CODES.O)
fun ComposeTestRule.eyesTest(testName: String, tagsFunc: Eyes.() -> Unit) {
  ComposeEyes(this).apply {
    apiKey = requireNotNull(BuildConfig.APPLITOOLS_API_KEY) { "Applitools API key is missing" }

    BuildConfig.APPLITOOLS_BATCH_ID.also { batchId ->
      if (batchId != null) {
        setBatch(BatchInfo(null).apply { id = batchId })
      }
    }

    componentsProvider = AndroidXComponentsProvider()

    try {
      open("BookmarkManager", testName)
      tagsFunc(this)
      close()
    } finally {
      abortIfNotClosed()
    }
  }
}
