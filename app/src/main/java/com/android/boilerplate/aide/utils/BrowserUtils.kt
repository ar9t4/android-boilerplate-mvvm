package com.android.boilerplate.aide.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.android.boilerplate.R

/**
 * @author Abdul Rahman
 */
object BrowserUtils {

    fun openInAppBrowser(context: Context, url: String) {
        val tabsIntentBuilder = CustomTabsIntent.Builder()
        tabsIntentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.gray_E9EEF1_black_1D1D1D))
        val tabsIntent = tabsIntentBuilder.build()
        tabsIntent.launchUrl(context, Uri.parse(url))
    }
}