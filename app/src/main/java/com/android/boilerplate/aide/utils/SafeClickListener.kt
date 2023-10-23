package com.android.boilerplate.aide.utils

import android.os.SystemClock
import android.view.View

/**
 * @author Abdul Rahman
 * @param defaultInterval default interval of 1000 between two click events
 * @param onSafeClick higher order function i.e delegate pointing to original callback
 */
class SafeClickListener(
    private val defaultInterval: Int,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickedTime: Long = 0

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime().minus(lastClickedTime) < defaultInterval) {
            return
        }
        lastClickedTime = SystemClock.elapsedRealtime()
        onSafeClick(view)
    }
}