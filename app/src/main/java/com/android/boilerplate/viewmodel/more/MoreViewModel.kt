package com.android.boilerplate.viewmodel.more

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.repository.more.MoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
@HiltViewModel
class MoreViewModel @Inject constructor(private val repository: MoreRepository) : BaseViewModel() {

    val moreItems = repository.getMoreItems()

    fun fetchMoreItems(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchMoreItems(context)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }
}