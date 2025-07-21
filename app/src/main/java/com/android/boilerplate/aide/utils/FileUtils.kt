package com.android.boilerplate.aide.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.boilerplate.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @author Abdul Rahman
 */
object FileUtils {

    private const val FILENAME_FORMAT = "yyyyMMddHHmmssSSS"
    private const val PHOTO_EXTENSION = ".jpg"
    private const val VIDEO_EXTENSION = ".mp4"
    private val TAG = this::class.java.simpleName

    /**
     * Checks if the given path is a file or directory
     */
    fun isFile(path: String?): Boolean {
        path ?: return false
        return File(path).isFile
    }

    /**
     * Generate name for Image File
     */
    fun getImageFileName(context: Context): String {
        context.apply {
            return getString(R.string.app_name_uppercase).plus(getString(R.string.image)).plus(
                SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
            ).plus(PHOTO_EXTENSION)
        }
    }

    /**
     * Generate name for Video File
     */
    fun getVideoFileName(context: Context): String {
        context.apply {
            return getString(R.string.app_name_uppercase).plus(getString(R.string.video)).plus(
                SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
            ).plus(VIDEO_EXTENSION)
        }
    }

    /** Helper function used to create a timestamped file */
    fun createFile(baseFolder: File, fileName: String): File {
        return File(baseFolder, fileName)
    }

    /**
     * Convert given bitmap into given file
     */
    fun convertBitmapToFile(bitmap: Bitmap?, file: File?): File? {
        bitmap ?: return null
        file ?: return null
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            Log.e(TAG, "convertBitmapToFile exception: $e")
            null
        }
    }
}