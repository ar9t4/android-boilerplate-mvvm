package com.android.boilerplate.base.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * @author Abdul Rahman
 */
abstract class BaseAdapter<B : ViewDataBinding, T, VH : BaseViewHolder<B>>
    (diffUtil: DiffUtil.ItemCallback<T>) : ListAdapter<T, BaseViewHolder<B>>(diffUtil) {

    abstract fun getInflater(): LayoutInflater

    abstract fun getLayoutId(viewType: Int = 1): Int

    abstract fun createViewHolder(binding: B): BaseViewHolder<B>

    abstract fun itemViewType(position: Int): Int

    abstract fun viewRecycled(holder: BaseViewHolder<B>)

    private fun inflateView(parent: ViewGroup, viewType: Int): B {
        return DataBindingUtil.inflate(getInflater(), getLayoutId(viewType), parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
        return createViewHolder(inflateView(parent, viewType))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<B>, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(position)
    }

    override fun onViewRecycled(holder: BaseViewHolder<B>) {
        viewRecycled(holder)
        super.onViewRecycled(holder)
    }
}