package com.android.boilerplate.aide.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import com.android.boilerplate.aide.utils.SafeClickListener

/**
 * @author Abdul Rahman
 */

/**
 * Make this view visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Check if this view is visible
 */
fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

/**
 * Make this view invisible
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Check if this view is invisible
 */
fun View.isInVisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

/**
 * Make this view gone
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Check if this view is gone
 */
fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

/**
 * Disable this view
 */
fun View.disable() {
    this.apply {
        isEnabled = false
        alpha = 0.5f
    }
}

/**
 * Enable this view
 */
fun View.enable() {
    this.apply {
        isEnabled = true
        alpha = 1.0f
    }
}

/**
 * Expand calling view
 */
fun View.expand() {
    val matchParentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(
        (this.parent as View).width,
        View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec: Int =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight: Int = this.measuredHeight
    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            this@expand.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
                else (targetHeight * interpolatedTime).toInt()
            this@expand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

/**
 * Collapse calling view
 */
fun View.collapse() {
    val initialHeight: Int = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                this@collapse.visibility = View.GONE
            } else {
                this@collapse.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

/**
 * @param onSafeClick delegate with view parameter to perform on safe click
 */
fun View.setSafeOnClickListener(defaultInterval: Int = 2000, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}