package com.android.boilerplate.view.settings.theme

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.boilerplate.R
import com.android.boilerplate.aide.extensions.gone
import com.android.boilerplate.aide.extensions.invisible
import com.android.boilerplate.base.view.BaseAdapter
import com.android.boilerplate.base.view.BaseViewHolder
import com.android.boilerplate.databinding.ItemThemeBinding
import com.android.boilerplate.model.data.aide.Theme

/**
 * @author Abdul Rahman
 */
class ThemesAdapter(
    private val context: Context,
    private val listener: ((theme: Theme) -> Unit)? = null
) : BaseAdapter<ViewDataBinding, Theme, BaseViewHolder<ViewDataBinding>>(
    ThemeDiffCallback()
) {

    override fun getInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_theme
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(binding: ViewDataBinding): BaseViewHolder<ViewDataBinding> {
        return ThemeViewHolder(binding as ItemThemeBinding) as BaseViewHolder<ViewDataBinding>
    }

    override fun itemViewType(position: Int): Int {
        return 1
    }

    override fun viewRecycled(holder: BaseViewHolder<ViewDataBinding>) {
        val viewHolder = holder as RecyclerView.ViewHolder
        if (viewHolder is ThemeViewHolder) {
            viewHolder.binding.apply {
                ivMarker.invisible()
                tvLanguage.text = null
                ivSelected.invisible()
            }
        }
    }

    inner class ThemeViewHolder(val binding: ItemThemeBinding) :
        BaseViewHolder<ItemThemeBinding>(binding) {

        override fun bind(position: Int) {
            binding.apply {
                val itemToBind = getItem(position)
                item = itemToBind
                executePendingBindings()
                if (position == itemCount.minus(1)) {
                    divider.gone()
                }
                itemView.setOnClickListener { listener?.invoke(itemToBind) }
            }
        }
    }

    private class ThemeDiffCallback : DiffUtil.ItemCallback<Theme>() {
        override fun areItemsTheSame(oldItem: Theme, newItem: Theme): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Theme, newItem: Theme): Boolean {
            return oldItem == newItem
        }
    }
}