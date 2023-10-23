package com.android.boilerplate.aide.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.util.Log
import android.util.Size
import java.io.File

/**
 * @author Abdul Rahman
 */
object ThumbnailUtils {

    private val TAG = this::class.java.simpleName

    /**
     * Creates thumbnail from given image file
     */
    fun createImageThumbnail(file: File, size: Size): Bitmap? {
        return try {
            return if (Utils.isQPlus()) {
                ThumbnailUtils.createImageThumbnail(file, size, null)
            } else {
                ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(file.path), size.width, size.height
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "createImageThumbnail exception: $e")
            null
        }
    }

    /**
     * Creates thumbnail from given video file
     */
    fun createVideoThumbnail(file: File, size: Size): Bitmap? {
        return try {
            return if (Utils.isQPlus()) {
                ThumbnailUtils.createVideoThumbnail(file, size, null)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "createVideoThumbnail exception: $e")
            null
        }
    }
}