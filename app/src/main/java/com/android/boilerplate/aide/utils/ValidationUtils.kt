package com.android.boilerplate.aide.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Abdul Rahman
 */
object ValidationUtils {

    private const val PATTERN_NAME = "^[A-Z a-z]*$"
    private const val PATTERN_FIRST_AND_OR_LAST_NAME = "^([A-Za-z]*)\\s?([A-Za-z]*?)\$"
    private const val PATTERN_CODE = "^[0-9A-Za-z]*$"

    // accept at least one uppercase, one lowercase alphabet, one number and one special character
    // and within a range 0f 8-20 characters
    // original pattern "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$"
    private const val PATTERN_PASSWORD =
        "^(?=.*[0-9])(?=.*[A-z])(?=.*[@#$%^[_]{|}&+=.,%~(*)-/:;<=>?!])(?=\\S+$).{8,20}$"

    private fun isValidPattern(text: CharSequence, stringPattern: String): Boolean {
        val pattern = Pattern.compile(stringPattern)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun isValidPassword(password: CharSequence): Boolean {
        val pattern: Pattern = Pattern.compile(PATTERN_PASSWORD)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidCode(code: CharSequence): Boolean {
        val pattern: Pattern = Pattern.compile(PATTERN_CODE)
        val matcher: Matcher = pattern.matcher(code)
        return matcher.matches()
    }

    fun isValidName(text: CharSequence): Any {
        if (TextUtils.isEmpty(text.trim())) {
            return false
        } else if (!isValidPattern(text, PATTERN_FIRST_AND_OR_LAST_NAME)) {
            return false
        }
        return true
    }

    fun isValidEmail(text: CharSequence): Any {
        if (TextUtils.isEmpty(text.trim())) {
            return false
        } else if (!isValidPattern(text, Patterns.EMAIL_ADDRESS.toString())) {
            return false
        }
        return true
    }
}