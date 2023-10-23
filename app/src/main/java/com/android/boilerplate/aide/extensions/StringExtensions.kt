package com.android.boilerplate.aide.extensions

import android.util.Log
import android.webkit.URLUtil
import java.util.*

/**
 * @author Abdul Rahman
 */

fun String.capitalizeFirstChar(): String {
    return this.substring(0, 1).uppercase(Locale.ROOT).plus(this.substring(1))
}

/**
 * Returns true if url is a photo url
 */
fun String.isImageUrl(): Boolean {
    this.lowercase(Locale.getDefault()).apply {
        return URLUtil.isValidUrl(this) && (endsWith(".bmp") || endsWith(".gif") ||
                endsWith(".jpg") || endsWith(".jpeg") ||
                endsWith(".png") || endsWith(".webp") ||
                endsWith(".heic") || endsWith(".heif"))
    }
}

/**
 * Returns true if url is a video url
 */
fun String.isVideoUrl(): Boolean {
    this.lowercase(Locale.getDefault()).apply {
        return URLUtil.isValidUrl(this) && (endsWith(".3gp") || endsWith(".mp4") ||
                endsWith(".mp4"))
    }
}

/**
 * Rounds given string to two decimal places
 */
fun String.roundToTwoDecimalPlaces(): String {
    return try {
        val number = this.toDouble()
        String.format("%.2f", number)
    } catch (exception: Exception) {
        Log.e("StringExtensions", "roundToTwoDecimalPlaces: $exception")
        this
    }
}