package com.android.boilerplate.aide.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * @author Abdul Rahman
 */
object ImageUtils {

    fun loadImage(
        imageView: ImageView,
        url: String?,
        @DrawableRes resourceId: Int = -1,
        @DrawableRes errorResourceId: Int = -1,
        circle: Boolean = false,
        onLoadFailed: (() -> Unit)? = null,
        onResourceReady: (() -> Unit)? = null
    ) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(resourceId)
            .error(errorResourceId)
            .apply(if (circle) RequestOptions().circleCrop() else RequestOptions().centerCrop())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadFailed?.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    onResourceReady?.invoke()
                    return false
                }
            })
            .into(imageView)
    }
}