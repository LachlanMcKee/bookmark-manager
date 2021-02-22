package net.lachlanmckee.bookmark.testing.util.eyes

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import com.applitools.eyes.android.common.BatchInfo
import com.applitools.eyes.android.common.EyesScreenshot
import com.applitools.eyes.android.common.Region
import com.applitools.eyes.android.components.androidx.AndroidXComponentsProvider
import com.applitools.eyes.android.espresso.Eyes
import com.applitools.eyes.android.espresso.utils.ImageUtils
import net.lachlanmckee.bookmark.BuildConfig

class ComposeEyes(private val composeTestRule: ComposeTestRule) : Eyes() {
  override fun getScreenshot(includeAllLayers: Boolean, hideCaret: Boolean): EyesScreenshot {
    composeTestRule.waitForIdle()
    val asAndroidBitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
    return BitmapScreenshot(asAndroidBitmap)
  }

  private class BitmapScreenshot(private val bitmap: Bitmap) :
    EyesScreenshot(ImageUtils.bitmapToBytes(bitmap)) {

    override fun getSubScreenshot(region: Region): EyesScreenshot {
      throw NotImplementedError("Not currently needed")
    }

    override fun getWidth() = bitmap.width

    override fun getHeight() = bitmap.height
  }
}

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
