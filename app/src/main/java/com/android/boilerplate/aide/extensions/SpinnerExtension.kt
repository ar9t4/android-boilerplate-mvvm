package com.android.boilerplate.aide.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

/**
 * @author Abdul Rahman
 */

fun Spinner.onItemSelectedListener(
    onItemSelectedListener: (p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) -> Unit
) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            onItemSelectedListener(p0, p1, p2, p3)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}