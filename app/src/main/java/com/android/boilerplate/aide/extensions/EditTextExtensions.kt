package com.android.boilerplate.aide.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author Abdul Rahman
 */

fun EditText.textWatcher(
    onTextChanged: (p0: CharSequence?, p1: Int, p2: Int, p3: Int) -> Unit,
    afterTextChanged: ((p0: Editable?) -> Unit)? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged(p0, p1, p2, p3)
        }

        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged?.let {
                it(p0)
            }
        }
    })
}