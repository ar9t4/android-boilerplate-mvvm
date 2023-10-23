package com.android.boilerplate.view.user

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.boilerplate.R
import com.android.boilerplate.base.view.BaseAdapter
import com.android.boilerplate.base.view.BaseViewHolder
import com.android.boilerplate.databinding.ItemRandomUserBinding
import com.android.boilerplate.model.data.local.database.entities.RandomUser

/**
 * @author Abdul Rahman
 */
class RandomUsersAdapter(
    private val context: Context,
    private val listener: ((randomUser: RandomUser) -> Unit)? = null
) : BaseAdapter<ViewDataBinding, RandomUser,
        BaseViewHolder<ViewDataBinding>>(RandomUserDiffCallback()) {

    override fun getInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_random_user
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(binding: ViewDataBinding): BaseViewHolder<ViewDataBinding> {
        return RandomUserViewHolder(binding as ItemRandomUserBinding)
                as BaseViewHolder<ViewDataBinding>
    }

    override fun itemViewType(position: Int): Int {
        return 1
    }

    override fun viewRecycled(holder: BaseViewHolder<ViewDataBinding>) {
        val viewHolder = holder as RecyclerView.ViewHolder
        if (viewHolder is RandomUserViewHolder) {
            viewHolder.binding.apply {
                ivPicture.setImageDrawable(null)
                tvName.text = null
                tvEmail.text = null
                tvPhone.text = null
            }
        }
    }

    inner class RandomUserViewHolder(val binding: ItemRandomUserBinding) :
        BaseViewHolder<ItemRandomUserBinding>(binding) {

        override fun bind(position: Int) {
            binding.apply {
                val userToBind = getItem(position)
                randomUser = userToBind
                executePendingBindings()
                itemView.setOnClickListener {
                    listener?.let {
                        it(userToBind)
                    }
                }
            }
        }
    }

    private class RandomUserDiffCallback : DiffUtil.ItemCallback<RandomUser>() {
        override fun areItemsTheSame(oldItem: RandomUser, newItem: RandomUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RandomUser, newItem: RandomUser): Boolean {
            return oldItem == newItem
        }
    }
}