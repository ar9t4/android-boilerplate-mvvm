package com.android.boilerplate.model.repository.more

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.boilerplate.R
import com.android.boilerplate.model.data.aide.MoreItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class MoreRepositoryImp @Inject constructor(@ApplicationContext private val context: Context) :
    MoreRepository {

    private val moreItems: MutableLiveData<List<MoreItem>?> = MutableLiveData(null)

    override fun getMoreItems(): LiveData<List<MoreItem>?> = moreItems

    override suspend fun fetchMoreItems() {
        val items = mutableListOf<MoreItem>()
        items.apply {
            context.apply {
                add(
                    MoreItem(
                        id = 1,
                        name = getString(R.string.settings),
                        icon = R.drawable.ic_settings
                    )
                )
                add(
                    MoreItem(
                        id = 2,
                        name = getString(R.string.feedback),
                        icon = R.drawable.ic_feedback_more
                    )
                )
                add(
                    MoreItem(
                        id = 3,
                        name = getString(R.string.privacy_policy),
                        icon = R.drawable.ic_privacy_policy
                    )
                )
                add(
                    MoreItem(
                        id = 4,
                        name = getString(R.string.share),
                        icon = R.drawable.ic_share
                    )
                )
                add(
                    MoreItem(
                        id = 5,
                        name = getString(R.string.rate_us),
                        icon = R.drawable.ic_rate_us
                    )
                )
                add(
                    MoreItem(
                        id = 6,
                        name = getString(R.string.more_apps),
                        icon = R.drawable.ic_more_apps
                    )
                )
                add(
                    MoreItem(
                        id = 7,
                        name = getString(R.string.version),
                        icon = R.drawable.ic_version
                    )
                )
            }
        }
        this.moreItems.postValue(items)
    }
}