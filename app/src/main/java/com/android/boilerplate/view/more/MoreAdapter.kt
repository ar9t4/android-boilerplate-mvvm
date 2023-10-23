package com.android.boilerplate.view.more

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.boilerplate.R
import com.android.boilerplate.aide.extensions.gone
import com.android.boilerplate.aide.extensions.visible
import com.android.boilerplate.base.view.BaseAdapter
import com.android.boilerplate.base.view.BaseViewHolder
import com.android.boilerplate.databinding.ItemMoreBinding
import com.android.boilerplate.databinding.ItemMoreVersionBinding
import com.android.boilerplate.model.data.aide.MoreItem

/**
 * @author Abdul Rahman
 */
class MoreAdapter(
    private val context: Context,
    private val listener: ((more: MoreItem) -> Unit)? = null
) : BaseAdapter<ViewDataBinding, MoreItem, BaseViewHolder<ViewDataBinding>>(MoreDiffCallback()) {

    override fun getInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_more
            else -> R.layout.item_more_version
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(binding: ViewDataBinding): BaseViewHolder<ViewDataBinding> {
        val viewHolder: BaseViewHolder<ViewDataBinding> =
            when (binding) {
                is ItemMoreBinding -> {
                    MoreViewHolder(binding) as BaseViewHolder<ViewDataBinding>
                }
                else -> {
                    MoreVersionViewHolder(binding as ItemMoreVersionBinding)
                            as BaseViewHolder<ViewDataBinding>
                }
            }
        return viewHolder
    }

    override fun itemViewType(position: Int): Int {
        return when {
            position != itemCount.minus(1) -> 1
            else -> 2
        }
    }

    override fun viewRecycled(holder: BaseViewHolder<ViewDataBinding>) {
        when (val viewHolder = holder as RecyclerView.ViewHolder) {
            is MoreViewHolder -> {
                viewHolder.binding.apply {
                    ivIcon.setImageDrawable(null)
                    tvName.text = null
                    ivArrowRight.visible()
                    divider.visible()
                }
            }
            is MoreVersionViewHolder -> {
                viewHolder.binding.apply {
                    ivIcon.setImageDrawable(null)
                    tvName.text = null
                    tvTitle.text = null
                }
            }
            else -> {

            }
        }
    }

    inner class MoreViewHolder(val binding: ItemMoreBinding) :
        BaseViewHolder<ItemMoreBinding>(binding) {

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

    inner class MoreVersionViewHolder(val binding: ItemMoreVersionBinding) :
        BaseViewHolder<ItemMoreVersionBinding>(binding) {

        override fun bind(position: Int) {
            binding.apply {
                val itemToBind = getItem(position)
                item = itemToBind
                executePendingBindings()
                itemView.setOnClickListener { listener?.invoke(itemToBind) }
            }
        }
    }

    private class MoreDiffCallback : DiffUtil.ItemCallback<MoreItem>() {
        override fun areItemsTheSame(oldItem: MoreItem, newItem: MoreItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoreItem, newItem: MoreItem): Boolean {
            return oldItem == newItem
        }
    }
}