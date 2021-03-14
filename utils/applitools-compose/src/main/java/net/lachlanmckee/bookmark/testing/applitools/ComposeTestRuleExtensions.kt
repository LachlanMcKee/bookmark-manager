package net.lachlanmckee.bookmark.testing.applitools

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.applitools.eyes.android.common.BatchInfo
import com.applitools.eyes.android.components.androidx.AndroidXComponentsProvider
import net.lachlanmckee.bookmark.util.applitools.BuildConfig

@RequiresApi(Build.VERSION_CODES.O)
fun ComposeTestRule.eyesTest(testName: String, tagsFunc: EyesWrapper.() -> Unit) {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
    // Cannot currently take screenshots of Composables below Oreo.
    return
  }

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
      tagsFunc(EyesWrapperImpl(this))
      close()
    } finally {
      abortIfNotClosed()
    }
  }
}
