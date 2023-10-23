package com.android.boilerplate.view.settings

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
import com.android.boilerplate.databinding.ItemSettingBinding
import com.android.boilerplate.databinding.ItemSettingNotificationBinding
import com.android.boilerplate.model.data.aide.SettingItem

/**
 * @author Abdul Rahman
 */
class SettingsAdapter(
    private val context: Context,
    private val listener: ((setting: SettingItem) -> Unit)? = null
) : BaseAdapter<ViewDataBinding, SettingItem, BaseViewHolder<ViewDataBinding>>(SettingDiffCallback()) {

    override fun getInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_setting
            else -> R.layout.item_setting_notification
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(binding: ViewDataBinding): BaseViewHolder<ViewDataBinding> {
        val viewHolder: BaseViewHolder<ViewDataBinding> =
            when (binding) {
                is ItemSettingBinding -> {
                    SettingViewHolder(binding) as BaseViewHolder<ViewDataBinding>
                }
                else -> {
                    SettingNotificationViewHolder(binding as ItemSettingNotificationBinding)
                            as BaseViewHolder<ViewDataBinding>
                }
            }
        return viewHolder
    }

    override fun itemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.key) {
            context.getString(R.string.notifications) -> 2
            else -> 1
        }
    }

    override fun viewRecycled(holder: BaseViewHolder<ViewDataBinding>) {
        when (val viewHolder = holder as RecyclerView.ViewHolder) {
            is SettingViewHolder -> {
                viewHolder.binding.apply {
                    tvKey.text = null
                    tvValue.text = null
                    divider.visible()
                }
            }
            is SettingNotificationViewHolder -> {
                viewHolder.binding.apply {
                    tvKey.text = null
                    tvValue.text = null
                    ivSwitch.setImageResource(R.drawable.ic_unchecked)
                    divider.visible()
                }
            }
            else -> {

            }
        }
    }

    inner class SettingViewHolder(val binding: ItemSettingBinding) :
        BaseViewHolder<ItemSettingBinding>(binding) {

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

    inner class SettingNotificationViewHolder(val binding: ItemSettingNotificationBinding) :
        BaseViewHolder<ItemSettingNotificationBinding>(binding) {

        override fun bind(position: Int) {
            binding.apply {
                val itemToBind = getItem(position)
                item = itemToBind
                executePendingBindings()
                if (itemToBind.value == context.getString(R.string.on)) {
                    ivSwitch.setImageResource(R.drawable.ic_checked)
                } else {
                    ivSwitch.setImageResource(R.drawable.ic_unchecked)
                }
                itemView.setOnClickListener { listener?.invoke(itemToBind) }
            }
        }
    }

    private class SettingDiffCallback : DiffUtil.ItemCallback<SettingItem>() {
        override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem == newItem
        }
    }
}