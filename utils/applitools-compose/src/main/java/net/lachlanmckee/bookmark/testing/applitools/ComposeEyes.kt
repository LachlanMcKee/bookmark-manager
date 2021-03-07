package net.lachlanmckee.bookmark.testing.applitools

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import com.applitools.eyes.android.common.EyesScreenshot
import com.applitools.eyes.android.common.Region
import com.applitools.eyes.android.espresso.Eyes
import com.applitools.eyes.android.espresso.utils.ImageUtils

@RequiresApi(Build.VERSION_CODES.O)
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
