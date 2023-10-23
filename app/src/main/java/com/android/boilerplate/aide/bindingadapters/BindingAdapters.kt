package com.android.boilerplate.aide.bindingadapters

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author Abdul Rahman
 */

@BindingAdapter(value = ["resId"])
fun loadDrawable(imageView: AppCompatImageView, resId: Int) {
    imageView.setImageResource(resId)
}

@BindingAdapter(value = ["url", "placeholder", "circle"], requireAll = false)
fun loadImage(
    imageView: AppCompatImageView,
    url: String,
    placeholder: Drawable,
    circle: Boolean = false
) {
    if (circle) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .apply(RequestOptions().circleCrop())
            .into(imageView)
    } else {
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(placeholder)
            .into(imageView)
    }
}