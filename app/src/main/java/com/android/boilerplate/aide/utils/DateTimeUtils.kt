package com.android.boilerplate.aide.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * @author Abdul Rahman
 */
object DateTimeUtils {

    private const val DATE_TIME_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val DATE_TIME_FORMAT_MMM_D_H_MM = "MMM d, h:mm a"
    const val DATE_TIME_FORMAT_MMM_D_YYYY = "MMM d, yyyy"

    fun convertUtcDateToFormat(utcDate: String, format: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = convertUtcTimeToMillis(utcDate)
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }

    fun convertSecToMinAndSec(seconds: Long): String {
        val min = (seconds % 3600) / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun convertUtcTimeToMillis(utcDate: String): Long {
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT_UTC, Locale.ENGLISH)
        val date = dateFormat.parse(utcDate)
        val timeZoneId = Calendar.getInstance().timeZone.id
        val offset = TimeZone.getTimeZone(timeZoneId).getOffset(date!!.time)
        val localDate = Date(date.time.plus(offset))
        return localDate.time
    }

    fun generateReadableTimeStamp(utcDate: String): String {
        convertUtcTimeToMillis(utcDate).let {
            return DateUtils.getRelativeTimeSpanString(
                it,
                System.currentTimeMillis(),
                0
            ).toString()
        }
    }
}