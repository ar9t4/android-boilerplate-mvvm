package com.android.boilerplate.model.repository.more

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.boilerplate.R
import com.android.boilerplate.base.model.repository.BaseRepositoryImp
import com.android.boilerplate.model.data.aide.MoreItem
import com.android.boilerplate.model.data.local.preference.Preferences
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class MoreRepositoryImp @Inject constructor(preferences: Preferences) : MoreRepository,
    BaseRepositoryImp(preferences) {

    private val moreItems = mutableListOf<MoreItem>()
    private val moreItemsData: MutableLiveData<List<MoreItem>?> = MutableLiveData(null)

    override fun getMoreItems(): LiveData<List<MoreItem>?> = moreItemsData

    override suspend fun fetchMoreItems(context: Context) {
        if (moreItemsData.value == null || hasLocaleChanged()) {
            moreItems.apply {
                // remove previous items
                clear()
                // add new items
                context.apply {
                    add(MoreItem(1, getString(R.string.settings), R.drawable.ic_settings))
                    add(MoreItem(2, getString(R.string.feedback), R.drawable.ic_feedback_more))
                    add(
                        MoreItem(
                            3,
                            getString(R.string.privacy_policy),
                            R.drawable.ic_privacy_policy
                        )
                    )
                    add(MoreItem(4, getString(R.string.share), R.drawable.ic_share))
                    add(MoreItem(5, getString(R.string.rate_us), R.drawable.ic_rate_us))
                    add(MoreItem(6, getString(R.string.more_apps), R.drawable.ic_more_apps))
                    add(MoreItem(7, getString(R.string.version), R.drawable.ic_version))
                }
            }
            moreItemsData.postValue(moreItems)
        }
    }
}