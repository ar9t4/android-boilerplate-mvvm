package com.android.boilerplate.model.repository.more

import androidx.lifecycle.LiveData
import com.android.boilerplate.model.data.aide.MoreItem

/**
 * @author Abdul Rahman
 */
interface MoreRepository {
    fun getMoreItems(): LiveData<List<MoreItem>?>
    suspend fun fetchMoreItems()
}