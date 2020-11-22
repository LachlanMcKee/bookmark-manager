package net.lachlanmckee.bookmark.feature

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

interface Navigator {
    fun openBookmark(url: String)
}

class NavigatorImpl @Inject constructor(
    @ActivityContext private val context: Context
) : Navigator {
    override fun openBookmark(bookmarkUrl: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bookmarkUrl)))
    }
}
