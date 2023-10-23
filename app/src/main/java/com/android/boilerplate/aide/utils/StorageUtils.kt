package com.android.boilerplate.aide.utils

import android.os.Environment
import android.os.StatFs

/**
 * @author Abdul Rahman
 */
object StorageUtils {

    fun getAvailableStorage(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.blockSizeLong.times(statFs.availableBlocksLong)
    }

    fun getTotalStorage(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.blockSizeLong.times(statFs.blockCountLong)
    }
}