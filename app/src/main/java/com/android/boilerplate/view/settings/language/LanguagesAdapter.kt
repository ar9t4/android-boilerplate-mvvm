package com.android.boilerplate.view.settings.language

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
import com.android.boilerplate.databinding.ItemLanguageBinding
import com.android.boilerplate.model.data.aide.Language

/**
 * @author Abdul Rahman
 */
class LanguagesAdapter(
    private val context: Context,
    private val listener: ((language: Language) -> Unit)? = null
) : BaseAdapter<ViewDataBinding, Language, BaseViewHolder<ViewDataBinding>>(
    LanguageDiffCallback()
) {

    override fun getInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_language
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(binding: ViewDataBinding): BaseViewHolder<ViewDataBinding> {
        return LanguageViewHolder(binding as ItemLanguageBinding) as BaseViewHolder<ViewDataBinding>
    }

    override fun itemViewType(position: Int): Int {
        return 1
    }

    override fun viewRecycled(holder: BaseViewHolder<ViewDataBinding>) {
        val viewHolder = holder as RecyclerView.ViewHolder
        if (viewHolder is LanguageViewHolder) {
            viewHolder.binding.apply {
                ivMarker.invisible()
                tvLanguage.text = null
                ivSelected.invisible()
            }
        }
    }

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        BaseViewHolder<ItemLanguageBinding>(binding) {

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

    private class LanguageDiffCallback : DiffUtil.ItemCallback<Language>() {
        override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem == newItem
        }
    }
}