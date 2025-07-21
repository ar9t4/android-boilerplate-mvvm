package com.android.boilerplate.aide.extensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Abdul Rahman
 */

/**
 * Copies outputStream into given inputStream
 */
fun OutputStream.copyInputStreamToFile(inputStream: InputStream?) {
    this.use { outputStream -> inputStream?.copyTo(outputStream) }
}

/**
 * Gets mimetype from given uri
 */
fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}

/**
 * Gets extension from given uri
 */
fun Context.getExtension(uri: Uri?): String? {
    uri ?: return null
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(getMimeType(uri)) ?: ""
}

/**
 * Gets file size from given uri
 * https://developer.android.com/training/secure-file-sharing/retrieve-info
 */
fun Context.getFileSize(uri: Uri?): Long {
    uri ?: return -1
    return contentResolver.query(
        uri, null, null, null, null
    )?.use { cursor ->
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()
        cursor.getLong(sizeIndex)
    } ?: -1
}

/**
 * Gets file name from given uri
 * https://developer.android.com/training/secure-file-sharing/retrieve-info
 */
fun Context.getFileName(uri: Uri?): String? {
    uri ?: return null
    return contentResolver.query(
        uri, null, null, null, null
    )?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    } ?: ""
}

/**
 * Creates a new file in memory and returns it
 */
fun Context.getFile(uri: Uri?): File? {
    uri ?: return null
    val file = File(
        cacheDir,
        getFileName(uri) ?: "temp_file_name_${System.currentTimeMillis()}.${getExtension(uri)}"
    )
    try {
        FileOutputStream(file).copyInputStreamToFile(contentResolver.openInputStream(uri))
        return file
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}